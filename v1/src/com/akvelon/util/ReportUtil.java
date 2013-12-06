package com.akvelon.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

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

	public static List<List<Report>> twoTaskInProgress(List<List<Report>> reports) {
		List<Report> res = new ArrayList<Report>();
		
		A :
		for (List<Report> reps : reports) {
			for (Report rep : reps) {
				if (rep.getReportName().equals("check bli de 1task in progress")) {
					Queue<Report> queue = new LinkedList<Report>(reps);
					while (!CollectionUtils.isEmpty(queue)) {
						Report r = queue.poll();
						for (Report rp : queue) {
							if (r.getBliID().equals(rp.getBliID())) {
								Report newRep = new Report();
								newRep.setBliID(r.getBliID());
								newRep.setBliLink(r.getBliLink());
								newRep.setBliName(r.getBliName());
								newRep.setBliOwner(r.getBliOwner());
								newRep.setReportName(r.getReportName());
								res.add(newRep);
							}
						}
					}
					reports.remove(reps);
					if (!CollectionUtils.isEmpty(res)) {
						reports.add(res);
					}
					return reports;
				} else {
					continue A;
				}
			}
		}

		return reports;
	}
	
	public static List<List<Report>> findTaskTestInProgress(List<List<Report>> reports) {
		List<List<Report>> res = new ArrayList<List<Report>>();
		List<String> rps = Arrays.asList("developer has 1tesk inprogress","developer has 1task inprogress");
		Report r = null;
		Set<String> temp = new HashSet<String>();
		
		A :
			for (int i = reports.size() - 1; i > 0; i--) {
				List<Report> reps = reports.get(i);
				for (Report rep : reps) {
				if (rps.contains(rep.getReportName())) {
						Queue<Report> queue = new LinkedList<Report>(reps);
						while (!CollectionUtils.isEmpty(queue)) {
							r = queue.poll();
							for (Report rp : queue) {
								if (r.getTaskOwner().equals(rp.getTaskOwner())) {
									temp.add(r.getTaskOwner());
								}
							}
						}
						List<Report> oneTaskTest = new ArrayList<Report>();
						for (Report rrp : reps) {
							if (temp.contains(rrp.getTaskOwner())) {
								oneTaskTest.add(rrp);
							}
						}
					if (!CollectionUtils.isEmpty(oneTaskTest)) {
						Collections.sort(oneTaskTest, new Comparator<Report>() {
							@Override
							public int compare(Report r1, Report r2) {
								return r1.getTaskOwner().compareTo(r2.getTaskOwner());
							}
						});
						res.add(oneTaskTest);
					}
					reports.remove(reps);
					continue A;
				} else {
					continue A;
					}
				}
			}

		reports.addAll(res);
		return reports;
	}
}
