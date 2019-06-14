package selling;

import java.time.LocalDateTime;
import java.util.ArrayList;
import history.History;
import history.HistoryManager;
import member.MemberManager;
import member.PointManager;
import menu.Menu;
import menu.MenuManager;
import util.Util;

public class SellManager implements Util {
	// 판매관리
	// 1. 주문
	// 주문을 하면 손님 객체를 생성하고 배열에 삽입.
	// 단, 해당 배열이 null 값이 아닐 때

	// 2. 결제
	// 테이블 번호에 해당하는 배열의 원소를 제거...
	// 결제할 때 회원 여부 확인
	// Y. 검색 후 해당하는 객체 포인트 차감
	// N. 그냥 계산
	// 내 잔고 오름.

	int select; // 입력값을 받을 변수.

	Menu m;

	// 장바구니 역할
	ArrayList<Menu> orderList = new ArrayList<Menu>();

	// 테이블에 빈 자리가 있는지 확인하는 변수
	boolean check = false;

	// 시작 메뉴
	public void selectSellMenu() {
		while (true) {

			System.out.println("판매관리");
			System.out.println("1. 주문 | 2. 결제 | 3.테이블 정보 | 4. 이전 메뉴");

			select = scan.nextInt();

			switch (select) {
			case 1:

				// 주문을 하기 전 자리 확인.
				if (selectTable() == false) {
					System.out.println("자리가 없어.");
				} else {
					order();
				}
				break;
			case 2:
				pay();
				break;
			case 3:
				showTable();
				break;
			case 4:
				return;
			}
		}
	}

	// 주문 받는 메서드.
	void order() {
		int i;

		// 추가 주문을 하면 계속해서 반복함.
		do {
			System.out.println("-----메뉴-----");
			for (i = 0; i < MenuManager.MenuList.size(); i++) {
				System.out.print(i + 1 + ". " + MenuManager.MenuList.get(i).getName() + " ");
			}
			System.out.println();

			select = scan.nextInt();

			m = MenuManager.MenuList.get(select - 1);

			check = whatOrderNum(m);

			orderList.add(m);

		} while (check == false);

		addTable();
	}

	// 테이블이 비어있으면 손님 객체를 생성하고 배열에추가하는 메서드.
	void addTable() {
		for (int i = 0; i < TABLE.length; i++) {
			if (TABLE[i] == null) {
				TABLE[i] = new Guest(orderList);

				orderList = new ArrayList<Menu>();
				return;

			}
		}

	}

	// 빈 자리를 찾는 메서드
	boolean selectTable() {
		boolean c = false;
		for (int i = 0; i < TABLE.length; i++) {
			if (TABLE[i] == null) {
				c = true;
			}
		}
		return c;
	}

	// 주문 메서드
	// 다른 메뉴도 주문할 때 false 값을 반환.
	boolean whatOrderNum(Menu m) {
		boolean addOrder = false;
		System.out.println("몇 개 주문해 ?");
		select = scan.nextInt();
		if (select < 1) {
			System.out.println("1개 이상 주문해");

		}

		else {

			for (int i = 0; i < select; i++) {
				m.ingredientMinus();

			}
		}
		System.out.println("다른 메뉴도 주문해?");
		System.out.println("1. 응, 2. 아니");
		select = scan.nextInt();

		switch (select) {

		case 1:
			addOrder = false;
			break;

		case 2:
			System.out.println("주문 완료.");
			addOrder = true;
			break;
		}
		return addOrder;
	}

	// 결제 메서드
	void pay() {
		System.out.println("몇 번 테이블 계산 할거야?");
		select = scan.nextInt();

		int tableNum = select - 1;
		if (select < TABLE.length + 1 && select > 0) {
			if (!(TABLE[tableNum] == null)) {
				int totalPrice = TABLE[tableNum].getTotalPrice();
				System.out.println(select + "번 테이블");
				System.out.println("결제 금액 : " + TABLE[tableNum].getTotalPrice());
				System.out.println("회원이야?");
				System.out.println("1. 응, 2. 아니");
				select = scan.nextInt();

				scan.nextLine();

				// 회원정보를 검색 후 포인트 적립 / 사용
				switch (select) {
				case 1:
					String message = MemberManager.getManager().showPoint();
					System.out.println(message);
					if (message == "회원정보가 존재하지 않습니다.") {
						System.out.println("가입하시겠습니까? 1.예 2.아니오 ");
						select = scan.nextInt();
						scan.nextLine();

						switch (select) { // 회원가입 여부에 대한 대답
						case 1:
							MemberManager.getManager().addMember();
							PointManager.getManager().addPoint(totalPrice);
							break;

						case 2:
							break;
						}

					} else {
						System.out.println("1. 포인트 사용 | 2. 포인트 적립");
						select = scan.nextInt();
						scan.nextLine();
						switch (select) {
						case 1:
							totalPrice = PointManager.getManager().usePoint(totalPrice);
							TABLE[tableNum].setTotalPrice(totalPrice);
							MyPOS.money += TABLE[tableNum].getTotalPrice();
							break;

						case 2:
							PointManager.getManager().addPoint(totalPrice);
							break;
						}
					}
					break;

				case 2:
					System.out.println("회원가입 하시겠습니까?");
					System.out.println("1. 응 | 2. 아니");
					select = scan.nextInt();
					scan.nextLine();

					if (select == 1) {
						MemberManager.getManager().addMember();
						System.out.println("포인트 적립할게");
						PointManager.getManager().addPoint(totalPrice);
						MyPOS.money += TABLE[tableNum].getTotalPrice();

					} else if (select == 2) {
						MyPOS.money += TABLE[tableNum].getTotalPrice();
						break;

					}
				}

				for (int i = 0; i < TABLE[tableNum].order.size(); i++) {

					// 히스토리 객체 생성

					long payAmount = (TABLE[tableNum].foodCnt[i] * TABLE[tableNum].order.get(i).getPrice());

					HistoryManager.insertHistory(TABLE[tableNum].payNum, LocalDateTime.now(),
							TABLE[tableNum].order.get(i).getName(), TABLE[tableNum].foodCnt[i], payAmount, "1111-1111");
					;
				}

				System.out.println("결제 완료");
				System.out.println("잔고 : " + MyPOS.money);
				TABLE[tableNum] = null;

			} else {
				System.out.println("없는 테이블");

			}
		}
	}

	void showTable() {
		System.out.println("=================================");
		for (int i = 0; i < TABLE.length; i++) {
			if (TABLE[i] != null) {
				System.out.println((i + 1) + "번 테이블 주문내역");
				TABLE[i].showInfo();
				System.out.println("=================================");
			}
		}
	}
}