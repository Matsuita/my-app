package jp.iglobe.pbl.controller.account;

import java.util.List;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.iglobe.pbl.model.account.Account;
import jp.iglobe.pbl.model.account.AccountDeleteForm;
import jp.iglobe.pbl.model.account.AccountSearchForm;
import jp.iglobe.pbl.model.account.AccountUpdateForm;
import jp.iglobe.pbl.service.SearchAccountService;

@Controller
@SessionAttributes("accountSearchForm")
public class SearchController {

	@ModelAttribute("accountSearchForm")
	public AccountSearchForm setUpForm() {
		return new AccountSearchForm();
	}

	@Autowired
	private SearchAccountService service;

	@GetMapping("/accounts/search")
	public String search(HttpSession session, Model model) {

		Account loginUser = (Account) session.getAttribute("loginUser");
		if (loginUser.getAccountsAuthority() < 1) {
			return "redirect:/dashboard";
		}

		model.addAttribute("accountSearchForm", new AccountSearchForm());
		return "S0040";
	}

	@PostMapping("/accounts/result")
	public String search(@Valid @ModelAttribute AccountSearchForm form,
			BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			model.addAttribute("accountSearchForm", form);
			return "S0040";
		}

		List<Account> list = service.search(form);

		model.addAttribute("accounts", list);
		model.addAttribute("accountSearchForm", form);
		return "S0041";
	}

	@GetMapping("/accounts/edit/{id}")
	public String edit(@PathVariable Integer id,
			HttpSession session,
			Model model) {

		Account loginUser = (Account) session.getAttribute("loginUser");
		if (loginUser.getAccountsAuthority() < 1) {
			return "redirect:/dashboard";
		}
		if (loginUser.getAccountsAuthority() == 1) {
			return "redirect:/accounts/search";
		}

		if (!model.containsAttribute("accountUpdateForm")) {
	        model.addAttribute("accountUpdateForm",
	                service.getAccountUpdateFormById(id));
	    }

		return "S0042";
	}

	@PostMapping("/accounts/edit/confirm")
	public String confirm(@Valid @ModelAttribute AccountUpdateForm form,
	        BindingResult result,
	        Model model) {

	    service.validateForConfirm(form, result);

	    if (result.hasErrors()) {
	        return "S0042";
	    }

	    if (form.getAccountId() == null) {
	        return "redirect:/accounts/search";
	    }

	    model.addAttribute("accountUpdateForm", form);
	    return "S0043";
	}
	@PostMapping("/accounts/update")
	public String update(@ModelAttribute AccountUpdateForm account,
			SessionStatus sessionStatus,
			HttpServletRequest request) {

		service.update(account);
		sessionStatus.setComplete();

		HttpSession session = request.getSession(false);
		if (session != null) {
			Account loginUser = (Account) session.getAttribute("loginUser");

			if (loginUser != null) {
				if (Objects.equals(loginUser.getAccountId(), account.getAccountId())) {
					session.invalidate();
					return "redirect:/";
				}
			}
		}

		return "redirect:/accounts/result";
	}

	@PostMapping("/accounts/delete")
	public String deleteConfirm(AccountDeleteForm form, Model model) {

		Account account = service.getAccountById(form.getAccountId());
		model.addAttribute("account", account);

		return "S0044";
	}

	@PostMapping("/accounts/delete/execute")
	public String deleteExecute(@ModelAttribute AccountDeleteForm account,
			SessionStatus sessionStatus) {

		service.logicalDelete(account.getAccountId());
		sessionStatus.setComplete();

		return "redirect:/accounts/result";
	}

	@GetMapping("/accounts/result")
	public String resultFromSession(HttpSession session,
			@ModelAttribute AccountSearchForm form,
			Model model) {

		Account loginUser = (Account) session.getAttribute("loginUser");
		if (loginUser.getAccountsAuthority() < 1) {
			return "redirect:/dashboard";
		}

		List<Account> list = service.search(form);
		model.addAttribute("accounts", list);

		return "S0041";
	}

	@GetMapping("/accounts/delete")
	public String deleteConfirmGet(HttpSession session,
			AccountDeleteForm form,
			Model model) {

		Account loginUser = (Account) session.getAttribute("loginUser");
		if (loginUser.getAccountsAuthority() < 1) {
			return "redirect:/dashboard";
		}
		if (loginUser.getAccountsAuthority() == 1) {
			return "redirect:/accounts/search";
		}

		if (form.getAccountId() == null) {
			return "redirect:/accounts/search";
		}

		Account account = service.getAccountById(form.getAccountId());
		model.addAttribute("account", account);

		return "S0044";
	}

	@GetMapping("/accounts/edit/confirm")
	public String confirmGet() {
		return "redirect:/accounts/search";
	}

	@PostMapping("/accounts/edit/cancel")
	public String editCancel() {
		return "redirect:/accounts/result";
	}

	@PostMapping("/accounts/edit/confirm/cancel")
	public String confirmCancel(
			@ModelAttribute("accountUpdateForm") AccountUpdateForm form,
			RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("accountUpdateForm", form);
		return "redirect:/accounts/edit/" + form.getAccountId();
	}
}