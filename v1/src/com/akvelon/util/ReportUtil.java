package com.akvelon.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akvelon.report.HourReport;
import com.akvelon.report.Report;

public class ReportUtil {

	/**
	 * Sort reports by bli owner
	 * 
	 * @param reports - list where each element is list with reports that belong
	 *            to one BLI
	 * @return list where each element is list with reports that belong to one
	 *         owner
	 */
	public static List<List<Report>> sortReportsByTaskOwner(List<List<Report>> reports) {
		Map<String, List<Report>> res = new HashMap<String, List<Report>>();
		for (List<Report> reps : reports) {
			for (Report rep : reps) {
				if (rep.getTaskOwner() != null) {
					if (!res.keySet().contains(rep.getTaskOwner())) {
						List<Report> listRep = new ArrayList<Report>();
						listRep.add(rep);
						res.put(rep.getTaskOwner(), listRep);
					} else {
						res.get(rep.getTaskOwner()).add(rep);
					}
				}
			}
		}
		return new ArrayList<List<Report>>(res.values());
	}
	
	public static List<HourReport> countActuals(List<HourReport> hReports) {
		Map<String, HourReport> res = new HashMap<String, HourReport>();
		HourReport temp = null;
		for (HourReport hRep : hReports) {
			if (!res.keySet().contains(hRep.getTeamMember())) {
				res.put(hRep.getTeamMember(), hRep);
			} else {
				temp = res.get(hRep.getTeamMember());
				temp.setReportedHours(temp.getReportedHours() + hRep.getReportedHours());
			}
		}
		return new ArrayList<HourReport>(res.values());
	}
	
	public static String createLink(AssetType assetType, String id) {
		return new StringBuilder()
				.append(String.format("https://www3.v1host.com/Tideworks/%s.mvc/Summary?oidToken=%s", assetType.name().toLowerCase(),
						assetType.name())).append("%3A").append(id).toString();
	}
	
	public static String normalizeName(String fullName) {
		return fullName.split("/")[1].split("\\.")[0].replace("_", " ");
	}
}
