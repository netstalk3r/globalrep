package com.akvelon.util;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;

import com.akvelon.report.HourReport;
import com.akvelon.report.Report;

public class TemplateConverter {

	private static final Logger log = Logger.getLogger(TemplateConverter.class);

	private static final String REP_TEMPLATE = "src/report_tepmlate.vm";
	private static final String BLI_OWNER_TEMPLATE = "src/bli_owner_tepmlate.vm";
	private static final String NO_REP_TEMPLATE = "src/no_reports.vm";

	private VelocityEngine velocityEng;
	private Template template;
	private VelocityContext context;

	public TemplateConverter() {
		velocityEng = new VelocityEngine();
		velocityEng.init();
	}

	public String convertToHTMLByRepType(List<List<Report>> reports) {
		return convert(REP_TEMPLATE, reports, ReportUtil.sortReportsByTaskOwner(reports), null);
	}

	public String convertToHTMLByTaskOwner(List<List<Report>> reports) {
		return convert(BLI_OWNER_TEMPLATE, ReportUtil.sortReportsByTaskOwner(reports), null, null);
	}

	public String convertToHTMLByRepTypeAndHourReps(List<List<Report>> reports, List<HourReport> hReports) {
		return convert(REP_TEMPLATE, reports, ReportUtil.sortReportsByTaskOwner(reports), hReports);
	}

	public String convertToHTMLNoRepotrs(List<HourReport> hReports) {
		return convert(NO_REP_TEMPLATE, null, null, hReports);
	}

	private String convert(String templ, List<List<Report>> repsByType, List<List<Report>> repsByOwner, List<HourReport> hReports) {
		template = velocityEng.getTemplate(templ);
		context = new VelocityContext();

		context.put("reports", repsByType);
		context.put("repsByOwner", repsByOwner);
		context.put("hReports", hReports);
		context.put("date", new DateTool());
		context.put("curDate", new Date());

		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		log.debug(writer.toString());
		return writer.toString();
	}
}
