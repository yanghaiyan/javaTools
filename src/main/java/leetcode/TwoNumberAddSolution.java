package leetcode;

class ListNode {

  int val;
  ListNode next;

  ListNode(int x) {
    val = x;
  }
}

public class TwoNumberAddSolution {

  public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    ListNode preNode = new ListNode(0);
    ListNode cur = preNode, p = l1, q = l2;
    int carry = 0;
    while (p != null || q != null) {
      int sum = 0;
      if (p == null) {
        sum = q.val + carry;
        q = q.next;
      } else if (q == null) {
        sum = p.val + carry;
        p = p.next;
      } else {
        sum = q.val + p.val + carry;
        q = q.next;
        p = p.next;
      }
      carry = sum / 10;
      cur.next = new ListNode(sum % 10);
      cur = cur.next;
    }
    if (carry == 1) {
      cur.next = new ListNode(carry);
    }
    return preNode.next;
  }

}

