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

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class WebscraperApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(WebscraperApplication.class, args);
		Document docu;
		try{
			docu = Jsoup.connect("https://www.mtsamples.com/site/pages/browse.asp?type=3-Allergy%20/%20Immunology").timeout(0).get();
		}catch (HttpStatusException e){
			System.out.println("http status exception, trying again.");
			docu = Jsoup.connect("https://www.mtsamples.com/site/pages/browse.asp?type=3-Allergy%20/%20Immunology").timeout(0).get();
		}
		String title = docu.title();
		System.out.println(title);

//		Connection connection = Jsoup.connect("https://www.mtsamples.com/site/pages/sample.asp?Type=5-Bariatrics&Sample=1061-Bariatric%20Consult%20-%20Surgical%20Weight%20Loss%20-%201");
//		connection.timeout(0);
//		Document docu2 = connection.get();
//		Elements element = docu2.getElementsByClass("hilightBold");
//		String content = element.text();
//		System.out.println(content);
//		MedicalTranscription medicalTranscription = formatText(content);
//		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//		String json = ow.writeValueAsString(medicalTranscription);
//		System.out.println(json);
//		System.out.println(medicalTranscription);

//		Elements elements = docu.getElementsByClass("dropdown-menu");
		for (Element a : docu.getElementsByTag("a")) {
//			if (a.className().equalsIgnoreCase("dropdown-item")) {
//				System.out.println(a.absUrl("href"));
//			}
			for (Element parent : a.parents()) {
				if (parent.tagName().equals("div") && parent.hasClass("collapse navbar-collapse")
						&& a.className().equalsIgnoreCase("dropdown-item")) {
					System.out.println("1"+a.absUrl("href"));

					Connection connection = Jsoup.connect(a.absUrl("href"));
					connection.userAgent("Mozilla/5.0");
					connection.timeout(0);
					Document docu2;
					try{
						docu2 = connection.get();
					}catch (HttpStatusException e){
						System.out.println("http excp trying again.");
						docu2 = connection.get();
					}
					for (Element a1 : docu2.getElementsByTag("a")) {
//			if (a.className().equalsIgnoreCase("dropdown-item")) {
//				System.out.println(a.absUrl("href"));
//			}
						for (Element parent1 : a1.parents()) {
							if (parent1.tagName().equals("div") && parent1.hasClass("collapse navbar-collapse")
									&& a1.className().equalsIgnoreCase("dropdown-item")) {
								System.out.println("2"+a1.attr("abs:href"));

								Connection connection1 = Jsoup.connect("https://www.mtsamples.com/site/pages/sample.asp?Type=3-Allergy%20/%20Immunology&Sample=386-Allergic%20Rhinitis");
								connection1.userAgent("Mozilla/5.0");
								connection1.timeout(0);
								Document docu3;
								try{
									docu3 = connection1.get();
									Elements element = docu3.getElementsByClass("hilightBold");
									String content1 = element.text();
									System.out.println(content1);
//									MedicalTranscription medicalTranscription = formatText(content);
//									ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//									String json = ow.writeValueAsString(medicalTranscription);
//									System.out.println(json);
//									System.out.println(medicalTranscription);
								}catch (HttpStatusException e){
									System.out.println("http excp trying again.");
									docu3 = connection1.get();
									Elements element = docu3.getElementsByClass("hilightBold");
									String content1 = element.text();
									System.out.println(content1);
//									MedicalTranscription medicalTranscription = formatText(content);
//									ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//									String json = ow.writeValueAsString(medicalTranscription);
//									System.out.println(json);
//									System.out.println(medicalTranscription);
								}
//								Elements element = docu3.getElementsByClass("hilightBold");
//								String content = element.text();
//								MedicalTranscription medicalTranscription = formatText(content);
//								ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//								String json = ow.writeValueAsString(medicalTranscription);
//								System.out.println(json);
//								System.out.println(medicalTranscription);
							}
						}
					}
//		Elements elements = docu.getElementsByTag("a");
//		for(Element elements1 : elements){
//			if (elements1.parent().tagName().equalsIgnoreCase("div") && elements1.parent().className().equalsIgnoreCase("dropdown-menu")){
//				System.out.println(elements1.absUrl("href"));
//			}
//		}
////		Elements allLinks = elements.tagName("a");
//		for (Element link : allLinks){
//			String absLink = link.absUrl("href");
//			System.out.println(absLink);
//		}

				}
			}}}
	public static MedicalTranscription formatText(String content){
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