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
	
	public static List<List<Report>> findCodeReviewBLI(List<List<Report>> reports) {
		List<Report> devDone = null;
		List<Report> reviewNotPassed = null;

		A: for (int i = reports.size() - 1; i > 0; i--) {
			if ("check bli with done dev tasks".equals(reports.get(i).get(0).getReportName())) {
				devDone = reports.get(i);
//				reports.remove(reports.get(i));
				continue A;
			} else if ("check task peer review not passed".equals(reports.get(i).get(0).getReportName())) {
				reviewNotPassed = reports.get(i);
//				reports.remove(reports.get(i));
				continue A;
			}
		}

		if (CollectionUtils.isEmpty(devDone)) return reports;
		List<Report> res = new ArrayList<Report>();
		boolean flag = true;
		
		for (Report r : devDone) {
			for (Report r2 : reviewNotPassed) {
				if (r.getBliID().equals(r2.getBliID())) {
					r.setReportName("check bli waiting fore code review");
					r.setTaskName(null);
					r.setTaskOwner(null);
					for (int i = 0, n = res.size(); i < n; i++) {
						if (r.getBliID().equals(res.get(i).getBliID())) {
							flag = false;
							break;
						}
					}
					if (flag) {
						res.add(r);
						flag = true;	
					}
				}
			}
		}
		
		reports.add(res);
		return reports;
	}
	
	/**
	 * We get all items/defects which have digit 3 in their story point. Here we
	 * removed items/defects with all valid story points and leave invalid.
	 */
	public static List<List<Report>> findValidStroryPoints(List<List<Report>> reports) {
		
		List<String> repsNames = Arrays.asList("check defect with invalid story points","check item with invalid story points");
		
		for (List<Report> reps : reports) {
			if (repsNames.contains(reps.get(0).getReportName())) {
				for (int i = 0; i < reps.size(); i++) {
					if (isStoryPointValid(reps.get(i))) {
						reps.remove(reps.get(i));
					}
				}
			}
		}
		return reports;
	}
	
	public static List<List<Report>> verifyTaskForMergeTest(List<List<Report>> reports) {
	
		List<String> repNames = Arrays.asList("check bli for merge tasks","check defect for merge tasks");
		
		for (List<Report> reps : reports) {
			if (repNames.contains(reps.get(0).getReportName())) {
				String project = reps.get(0).getProject().substring(2,reps.get(0).getProject().length()-2);
				for (int i = reps.size() - 1; i >= 0; i--) {
					if (hasTaskForMerge(reps.get(i),project)) {
						reps.remove(reps.get(i));
					}
				}
			}
		}
		
		return reports;
	}
	
	private static boolean isStoryPointValid(Report rep) {
		double storyPoint = Double.parseDouble(rep.getStoryPoints());
		int integer = (int) storyPoint;
		return storyPoint - integer == 0;
	}
	
	private static boolean hasTaskForMerge(Report report, String project) {
		int counter = 0;
		List<String> names = report.getTaskNames();
		for (String name : names) {
			if (name.indexOf(project) != -1) {
				counter++;
			}
		}
		return counter == 2;
	}
}
