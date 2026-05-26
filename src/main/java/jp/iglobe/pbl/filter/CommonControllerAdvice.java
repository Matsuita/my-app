package jp.iglobe.pbl.filter;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.iglobe.pbl.model.account.Account;

@ControllerAdvice
public class CommonControllerAdvice {

    @ModelAttribute
    public void addCommonAttributes(HttpSession session, Model model) {

        Object obj = session.getAttribute("loginUser");

        if (obj != null) {
            Account user = (Account) obj;

            model.addAttribute("isSalesEditable", user.getSalesAuthority() == 2);
            model.addAttribute("canViewSales", user.getSalesAuthority() >= 1);
            model.addAttribute("isAccountEditable", user.getAccountsAuthority() == 2);
            model.addAttribute("canViewAccounts", user.getAccountsAuthority() >= 1);
        }
    }

}
