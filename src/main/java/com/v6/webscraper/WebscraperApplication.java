package com.v6.webscraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.v6.model.MedicalTranscription;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class WebscraperApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(WebscraperApplication.class, args);
		Path currentWorkingDir = Paths.get("").toAbsolutePath();
		String filePath = currentWorkingDir+"src/main/resources/AllMedicalRecords.txt";
		System.out.println(currentWorkingDir.normalize().toString());
		try {
			File file = new File("AllMedicalRecords.txt");
			if (file.createNewFile()) {
				System.out.println("File created: " + file.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		FileWriter fileWriter = new FileWriter("AllMedicalRecords.txt");
		Document homePage;
		try {
			homePage = Jsoup.connect("https://mtsamples.com/").timeout(0).get();
		} catch (HttpStatusException e) {
			System.out.println("http status exception, trying again.");
			homePage = Jsoup.connect("https://mtsamples.com/").timeout(0).get();
		}
		String title = homePage.title();
		System.out.println(title);
		for (Element a : homePage.getElementsByTag("a")) {
			for (Element parent : a.parents()) {
				if (parent.tagName().equals("div") && parent.hasClass("collapse navbar-collapse")
						&& a.className().equalsIgnoreCase("dropdown-item")) {
					System.out.println("1" + a.absUrl("href"));
					Connection connection = Jsoup.connect(a.absUrl("href"));
					connection.userAgent("Mozilla/5.0");
					connection.timeout(0);
					Document subPageUnderEachCategory;
					try {
						subPageUnderEachCategory = connection.get();
					} catch (HttpStatusException e) {
						System.out.println("http excp trying again.");
						subPageUnderEachCategory = connection.get();
					}
					for (Element a1 : subPageUnderEachCategory.getElementsByTag("a")) {
						for (Element parent1 : a1.parents()) {
							if (parent1.tagName().equals("div") && parent1.hasClass("collapse navbar-collapse")
									&& a1.className().equalsIgnoreCase("dropdown-item")) {
								System.out.println("2" + a1.attr("abs:href"));
								Connection connection1 = Jsoup.connect(a1.attr("abs:href"));
								connection1.userAgent("Mozilla/5.0");
								connection1.timeout(0);
								Document finalPageForEachCategory;
								try {
									finalPageForEachCategory = connection1.get();
									Elements element = finalPageForEachCategory.getElementsByClass("hilightBold");
									String content1 = element.text();
									System.out.println("3" + content1);
									fileWriter.write(content1+"\n");
//									MedicalTranscription medicalTranscription = formatText(content);
//									ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//									String json = ow.writeValueAsString(medicalTranscription);
//									System.out.println(json);
//									System.out.println(medicalTranscription);
								} catch (HttpStatusException e) {
									System.out.println("http excp trying again.");
									finalPageForEachCategory = connection1.get();
									Elements element = finalPageForEachCategory.getElementsByClass("hilightBold");
									String content1 = element.text();
									System.out.println("3" + content1);
									fileWriter.write(content1+"\n");
//									MedicalTranscription medicalTranscription = formatText(content);
//									ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//									String json = ow.writeValueAsString(medicalTranscription);
//									System.out.println(json);
//									System.out.println(medicalTranscription);
								}
//								Elements element = finalPageForEachCategory.getElementsByClass("hilightBold");
//								String content = element.text();
//								MedicalTranscription medicalTranscription = formatText(content);
//								ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//								String json = ow.writeValueAsString(medicalTranscription);
//								System.out.println(json);
//								System.out.println(medicalTranscription);
								System.out.println("=============================================================================");
							}
						}
					}
				}
			}
		}
	}

	public static MedicalTranscription formatText(String content) {
		MedicalTranscription medicalTranscription = new MedicalTranscription();
		medicalTranscription.setMedicalSpecialty(content.substring(0, content.indexOf("SUBJECTIVE:")));
		medicalTranscription.setSubjective(content.substring(content.indexOf("SUBJECTIVE:"), content.indexOf("MEDICATIONS:")));
		medicalTranscription.setMedications(content.substring(content.indexOf("MEDICATIONS:"), content.indexOf("ALLERGIES:")));
		medicalTranscription.setAllergies(content.substring(content.indexOf("ALLERGIES:"), content.indexOf("OBJECTIVE:")));
		medicalTranscription.setVitals(content.substring(content.indexOf("Vitals:"), content.indexOf("HEENT:")));
		medicalTranscription.setHeent(content.substring(content.indexOf("HEENT:"), content.indexOf("Neck:")));
		medicalTranscription.setNeck(content.substring(content.indexOf("Neck:"), content.indexOf("Lungs:")));
		medicalTranscription.setAssessment(content.substring(content.indexOf("ASSESSMENT:"), content.indexOf("PLAN:")));
		medicalTranscription.setPlan(content.substring(content.indexOf("PLAN:"), content.indexOf("Keywords:")));
		medicalTranscription.setKeywords(content.substring(content.indexOf("Keywords:"), content.lastIndexOf(",")));
		return medicalTranscription;
	}
}