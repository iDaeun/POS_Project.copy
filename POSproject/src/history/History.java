package history;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class History {
	private int payNum;
	private LocalDateTime payTime;
	private String payItem;
	private int payEa;
	private long payAmount;
	private String memberId;

	DateTimeFormatter f1 = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
	DateTimeFormatter f2 = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

	public int getPayNum() {
		return payNum;
	}

	public void setPayNum(int payNum) {
		this.payNum = payNum;
	}

	public LocalDateTime getPayTime() {
		return payTime;
	}

	public void setPayTime(LocalDateTime payTime) {
		this.payTime = payTime;
	}

	public String getPayItem() {
		return payItem;
	}

	public void setPayItem(String payItem) {
		this.payItem = payItem;
	}

	public int getPayEa() {
		return payEa;
	}

	public void setPayEa(int payEa) {
		this.payEa = payEa;
	}

	public long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(long payAmount) {
		this.payAmount = payAmount;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public History(int payNum, LocalDateTime payTime, String payItem, int payEa, long payAmount, String memberId) {
		this.payNum = payNum; // 결제번호
		this.payTime = payTime; // 결제시각
		this.payItem = payItem; // 결제항목
		this.payEa = payEa; // 개수
		// 이따 변수 이름 나오면 곱하기로 바꾸기
		this.payAmount = payAmount; // 돈
		this.memberId = memberId; // 비회원은 guest로 들어간다

	}

	public void showHistory(int i) {

		System.out.print(i + "\t");

		System.out.printf(f2.format(payTime) + "\t");
		/*
		 * if (f2.format(payTime).length() >= 8) { System.out.printf(f2.format(payTime)
		 * + "\t"); } else { System.out.printf(f2.format(payTime) + "\t\t"); }
		 */
		System.out.print(payItem + "\t");
		System.out.print(payEa + "개\t");
		System.out.print(payAmount + "\t\t");
		System.out.print(memberId + "\t");
		// System.out.print(memberName+"\t");
		System.out.print(payNum + "\t");
		System.out.println(f1.format(payTime) + "\t");

	}

	// 폰번호로 멤버이름을 찾는 메소드가 필요하다
	public void phoneToName() {

	}

}
