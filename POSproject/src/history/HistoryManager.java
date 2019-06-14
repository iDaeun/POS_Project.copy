package history;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import util.Util;

public class HistoryManager {

	public HistoryManager() {
		insertHistory(1, LocalDateTime.now().minusDays(1), "멸추김밥", 2, 6000, "01031251111");
		insertHistory(2, LocalDateTime.now().minusDays(1).plusHours(1).plusMinutes(30), "메롱김밥", 1, 7000, "01011111111");
		insertHistory(2, LocalDateTime.now().minusDays(1).plusHours(2).plusMinutes(30), "또잉김밥", 2, 11000,
				"01011111111");
		insertHistory(3, LocalDateTime.now().minusDays(1).plusHours(3).plusMinutes(30), "메롱김밥", 5, 35000,
				"01012346554");
		insertHistory(5, LocalDateTime.now().plusDays(1), "야채김밥", 9, 26000, "01033337777");
		insertHistory(6, LocalDateTime.now().plusDays(2).plusHours(1).plusMinutes(30), "그냥김밥", 1, 3000, "01031251111");
		insertHistory(7, LocalDateTime.now().plusMonths(1), "그냥김밥", 1, 3000, "01031251753");
		insertHistory(8, LocalDateTime.now().plusMonths(1).plusDays(2).plusHours(1).plusMinutes(30), "먀아김밥", 5, 13000,
				"01031251111");
		insertHistory(9, LocalDateTime.now().plusMonths(1).plusDays(3).plusHours(10).plusMinutes(13), "피자김밥", 1, 3000,
				"01031251753");
	}

	static ArrayList<History> arr = new ArrayList<History>();

//	ArrayList<YearHistory> yearHistoryArr = new ArrayList<YearHistory>(); // 년도
//	MonthHistory[] monthHistoryArr = new MonthHistory[12]; // 월
//	DateHistory[] dateHistoryArr = new DateHistory[31]; // 일
//	Iterator<String> itr = ks.iterator();

	HashMap<String, DateHistory> dailyMap = new HashMap<>();
	Set<String> ks = dailyMap.keySet();

	DateTimeFormatter f1 = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL); // 2019년 5월 27일 월요일
	DateTimeFormatter f3 = DateTimeFormatter.ofPattern("yyyyMMdd"); // 20190527

	// 0. 히스토리 객체 생성해서 배열에 넣는 메소드
	public static void insertHistory(int payNum, LocalDateTime payTime, String payItem, int payEa, long payAmount,
			String memberId) {
		History history = new History(payNum, payTime, payItem, payEa, payAmount, memberId);
		arr.add(history);

	}

	// 5.전체출력 메소드
	public void showHistoryAll() {
		System.out.println("****************************  [저장된 결제내역 전체를 출력합니다.]  ********************************");
		System.out.println("No\t결제 시각\t결제 메뉴\t수량\t결제금액\t구매자\t\t결제번호\t결제 날짜");
		System.out
				.println("------------------------------------------------------------------------------------------");
		if (arr.size() < 1) {
			System.out.println("결제 내역이 없습니다.");
		} else {
			int cnt = 0;
			int tot = 0;
			int kimbobCnt = 0;

			for (int i = 0; i < arr.size(); i++) {
				cnt++;
				tot += arr.get(i).getPayAmount();
				kimbobCnt += arr.get(i).getPayEa();

				arr.get(i).showHistory(cnt);
			}
			System.out.println(
					"------------------------------------------------------------------------------------------");
			System.out.println("총 매출액 :" + tot + ", " + "팔린 김밥 개수 : " + kimbobCnt);

		}

	}

	// ================================================================
	// 1. 오늘의 결제내역을 보여주는 메소드
	public void showTodayHistory() {
		showDayHistoryAll(LocalDate.now());
	}
	// ================================================================

	// ================================================================
	// 2. 일일 결제내역을 보여주는 메소드(검색해서)

	public void showTitle() {
		System.out.println("No\t결제 시각\t결제 메뉴\t수량\t결제금액\t구매자\t\t결제번호");
		System.out
				.println("------------------------------------------------------------------------------------------");

	};

	public void showDayHistoryTitle(LocalDate date) {
		System.out.println(
				"******************************  [" + f1.format(date) + "의 결제내역]  *****************************");

		showTitle();
	}

	public void showMonthHistoryTitle(String yyyyMM) {
		System.out.println("******************************  [" + yyyyMM + "의 상세 결제내역  ]******************************");
		showTitle();
	}

	// 특정 날짜의 히스토리 알아내기
	// 받은걸 써야(수정해야함)
	public void showDayHistoryAll(LocalDate date) {

		showDayHistoryTitle(date);
		if (arr.size() < 1) {
			System.out.println("결제 내역이 없습니다.");
		} else {
			int cnt = 0;
			int tot = 0;
			int kimbobCnt = 0;
			for (int i = 0; i < arr.size(); i++) {
				if (arr.get(i).getPayTime().getDayOfYear() == date.getDayOfYear()) {
					tot += arr.get(i).getPayAmount();
					kimbobCnt += arr.get(i).getPayEa();
					cnt++;
					arr.get(i).showHistory(cnt);
				}
			}
			System.out.println(
					"------------------------------------------------------------------------------------------");
			System.out.println("총 매출액 :" + tot + ", " + "팔린 김밥 개수 : " + kimbobCnt);
		}

		System.out
				.println("******************************************************************************************");
		System.out.println();
		System.out.println();
	}

	// 미완된 기능
	public void makeDailyHistory(String yyyyMMdd) {
		// ArrayList<Product> pro = new ArrayList<Product>();
		DateHistory dh;

		// dailyMap.containsKey(yyyyMMdd)
		if (dailyMap.get(yyyyMMdd) == null) {
			dh = new DateHistory(yyyyMMdd);

			for (int i = 0; i < arr.size(); i++) {
				String item = arr.get(i).getPayItem();// 결제내역에서 뽑아온 항목의 이름

				if (f3.format(arr.get(i).getPayTime()).equals(yyyyMMdd)) {
					Product pro = new Product(yyyyMMdd, item, arr.get(i).getPayEa(), arr.get(i).getPayAmount());
					dh.productMap.put(item, pro);

					// 생성해서 프로덕트 맵에 넣었다 아이템명, 프로덕트
					if (dh.productMap.get(item).payItem.equals(item)) {
						dh.productMap.get(item).totPayEa += arr.get(i).getPayEa();
						dh.productMap.get(item).totPayEa += arr.get(i).getPayAmount();
					}
					// 특정날짜, 데일리 히스토리를 데일리맵에 넣었다
					dailyMap.put(yyyyMMdd, dh);
				}
				System.out.println(dailyMap.get(yyyyMMdd).productMap.get(item).payItem);
			}
		}

	}

	public void showDayHistory() {
		// 일별 결제내역
		System.out.println("어떤 날짜의 결제 내역을 출력할까요?(숫자 8자리로 입력해주세요 ex.20190527)");

		String date = Util.scan.nextLine().replaceAll(" ", "");

		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6));
		int day = Integer.parseInt(date.substring(6, 8));

		// LocalDateTime date = LocalDateTime.now();
		LocalDate yyyyMMdd = LocalDate.of(year, month, day);
		showDayHistoryAll(yyyyMMdd);
	}

	// ================================================================

	public void showMonthHistoryAll(String yyyyMM) {

		int year = Integer.parseInt(yyyyMM.substring(0, 4));
		int month = Integer.parseInt(yyyyMM.substring(4, 6));

		if (arr.size() < 1) {
			System.out.println("결제 내역이 없습니다.");
		} else {
			int cnt = 0;
			int tot = 0;
			int kimbobCnt = 0;
			showMonthHistoryTitle(yyyyMM);
			for (int i = 0; i < arr.size(); i++) {
				if ((month == arr.get(i).getPayTime().getMonthValue()) && (year == arr.get(i).getPayTime().getYear())) {
					tot += arr.get(i).getPayAmount();
					kimbobCnt += arr.get(i).getPayEa();
					cnt++;
					arr.get(i).showHistory(cnt);
				}
			}

			System.out.println(
					"------------------------------------------------------------------------------------------");
			System.out.println("총 매출액 :" + tot + ", " + "팔린 김밥 개수 : " + kimbobCnt);
		}
	}

	public void showMonthHistory() {

		// 월별 결제내역
		System.out.println("원하는 월을 입력해주세요.(숫자 6자리로 입력해주세요 ex.201905)");

		String yyyyMM = Util.scan.nextLine().replaceAll(" ", "");

		showMonthHistoryAll(yyyyMM);
	}

	//
	public void showMemberHistory() {
		// 일별 결제내역
		System.out.println("어떤 회원의 결제 내역을 출력할까요?");
		String member = Util.scan.nextLine();

		System.out.println(member + "님의 결제내역입니다");
		showTitle();
		int cnt = 0;
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).getMemberId().equals(member.trim())) {
				cnt++;
				arr.get(i).showHistory(cnt);
			}

		}

	}

	// ================================================================

	public void printMenu() {
		while (true) {
			System.out
					.println("=====================================  메뉴를 선택해주세요  ====================================");
			System.out.println("1.오늘의 결제내역\t 2.일별 검색\t 3.월별 결제내역 검색\t 4.회원별 결제내역\t 5.모든 결제내역 보기\t 6. 이전메뉴");
			// System.out.println("인기메뉴");
			System.out.println(
					"==========================================================================================");

			int choice = Util.scan.nextInt();

			Util.scan.nextLine();
			switch (choice) {
			case 1:
				showTodayHistory();
				break;
			case 2:
				showDayHistory();
				break;
			case 3:
				showMonthHistory();
				break;
			case 4:
				showMemberHistory();
				break;
			case 5:
				showHistoryAll();
				break;
			case 6:
				return;

			default:
				break;
			}
		}
	}

}
