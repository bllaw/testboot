package com.example.demo4.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

	public byte[] generatePDF(String template, JRBeanCollectionDataSource dataSource,
			Map<String, Object> parameters) {
		try (ByteArrayOutputStream pos = new ByteArrayOutputStream()) {
			final JasperReport report = loadJasperTemplate(template);
			JasperReportsUtils.renderAsPdf(report, parameters, dataSource, pos);
			return pos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create PDF", e);
		}
	}

	private JasperReport loadJasperTemplate(String template) throws JRException {
		log.info(String.format("Invoice template path : %s", template));
		final InputStream reportInputStream = getClass().getResourceAsStream(template);
		return (JasperReport) JRLoader.loadObject(reportInputStream);
	}

	@SuppressWarnings("unused")
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
		final Kugc kugc = userService.findKugcById(id);
		model.addAttribute("name", kugc.getKugc_cgcid() + " - " + kugc.getKugc_cgcname());
		return "kugc";
	}

	@GetMapping("/pdf")
	public ResponseEntity<byte[]> pdf() {
		final ArrayList<Data> list = new ArrayList<Data>();
		final Data data = new Data();
		data.setId(101L);
		data.setName("Test Me");
		data.setPrice(12345);
		list.add(data);
		list.add(data);
		list.add(data);
		list.add(data);
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
		final byte[] bytes = generatePDF(invoice_template_path2, dataSource, new HashMap<String, Object>());

		final HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    final String filename = "mydoc.pdf";
	    headers.add("Content-Disposition", "inline;filename=" + filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
	            bytes, headers, HttpStatus.OK);
	    return response;
	}
}
