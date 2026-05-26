package jp.iglobe.pbl.controller.dashboard;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.iglobe.pbl.service.DashboardService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService
        dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session,Model model) {

        // 未ログイン
        if(session.getAttribute("loginUser") == null) {
            return "redirect:/";
        }

        long todaySales =dashboardService.getTodaySales();
        long salesCount = dashboardService.getSalesCount();
        String topProduct = dashboardService.getTopProduct();
        String worstProduct = dashboardService.getWorstProduct();
        List<String> topProducts = dashboardService.getTopProducts();
        List<Integer> monthlySales = dashboardService.getMonthlySales();
        String todayMessage = dashboardService.getTodayMessage();
        long achievementRate = dashboardService.getAchievementRate(todaySales);
        String salesRank = dashboardService.getSalesRank(achievementRate);
        String salesComment = dashboardService.getSalesComment(achievementRate);
        model.addAttribute("todaySales", todaySales);
        model.addAttribute("salesCount", salesCount);
        model.addAttribute("topProduct", topProduct);
        model.addAttribute("worstProduct", worstProduct);
        model.addAttribute("topProducts", topProducts);
        model.addAttribute("todayMessage", todayMessage);
        model.addAttribute("achievementRate", achievementRate);
        model.addAttribute("salesRank", salesRank);
        model.addAttribute("salesComment", salesComment);
        model.addAttribute("jan", monthlySales.get(0));
        model.addAttribute("feb", monthlySales.get(1));
        model.addAttribute("mar", monthlySales.get(2));
        model.addAttribute("apr", monthlySales.get(3));
        model.addAttribute("may", monthlySales.get(4));
        model.addAttribute("jun", monthlySales.get(5));
        model.addAttribute("jul", monthlySales.get(6));
        model.addAttribute("aug", monthlySales.get(7));
        model.addAttribute("sep", monthlySales.get(8));
        model.addAttribute("oct", monthlySales.get(9));
        model.addAttribute("nov", monthlySales.get(10));
        model.addAttribute("dec", monthlySales.get(11));
        return "C0020";
    }
}