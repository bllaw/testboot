package com.example.demo4.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo4.domain.Data;
import com.example.demo4.domain.Kugc;
import com.example.demo4.domain.User;
import com.example.demo4.service.UserService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@Controller
public class GreetingController {
	private static final Log log = LogFactory.getLog(GreetingController.class);

	@Autowired
	private UserService userService;

	private final String invoice_template_path = "/report2.jrxml";
	private final String invoice_template_path2 = "/report2.jasper";

	public void generateInvoiceFor() throws IOException {
		File pdfFile = File.createTempFile("my-invoice", ".pdf");
		log.info(String.format("File : %s", pdfFile.getAbsolutePath()));
		try (FileOutputStream pos = new FileOutputStream(pdfFile)) {
			// Load the invoice jrxml template.
			final JasperReport report = loadTemplate2();
			// Create parameters map.
			final Map<String, Object> parameters = new HashMap<String, Object>();

			final ArrayList<Data> list = new ArrayList<Data>();
			final Data data = new Data();
			data.setId(101L);
			data.setName("Test Me");
			data.setPrice(12345);
			list.add(data);
			final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

			// Render the PDF file
			JasperReportsUtils.renderAsPdf(report, parameters, dataSource, pos);
		} catch (final Exception e) {
			log.error(String.format("An error occured during PDF creation: %s", e));
		}
	}

	private JasperReport loadTemplate2() throws JRException {
		log.info(String.format("Invoice template path : %s", invoice_template_path2));
		final InputStream reportInputStream = getClass().getResourceAsStream(invoice_template_path2);
		return (JasperReport) JRLoader.loadObject(reportInputStream);
	}

	private JasperReport loadTemplate() throws JRException {
		log.info(String.format("Invoice template path : %s", invoice_template_path));

		final InputStream reportInputStream = getClass().getResourceAsStream(invoice_template_path);
		final JasperDesign jasperDesign = JRXmlLoader.load(reportInputStream);

		return JasperCompileManager.compileReport(jasperDesign);
	}

	@GetMapping("/greeting")
	public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
			Model model) {
		final User user = userService.findUserById(1);
		final User user2 = userService.findUserById(15);
		System.out.println(user2);
		model.addAttribute("name", name + "XXX");
		model.addAttribute("user", user);
		return "greeting";
	}

	@GetMapping("/kugc")
	public String kugc(@RequestParam(name = "id", required = false, defaultValue = "M0032") String id, Model model)
			throws IOException {
		generateInvoiceFor();

		final Kugc kugc = userService.findKugcById(id);
		model.addAttribute("name", kugc.getKugc_cgcid() + " - " + kugc.getKugc_cgcname());
		return "kugc";
	}
}
