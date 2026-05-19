package jp.iglobe.pbl.controller.dashboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.iglobe.pbl.model.sales.Sale;
import jp.iglobe.pbl.repository.SalesRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor


public class DashboardController {
	

    private final SalesRepository salesRepository;

    @GetMapping("/dashboard")
    public String dashboard(
            HttpSession session,
            Model model) {

        // 未ログイン
        if(session.getAttribute(
                "loginUser") == null) {

            return "redirect:/";
        }

        // 売上一覧
        List<Sale> salesList =
                salesRepository.findAll();

        // 今日売上
        int todaySales =
                salesList.stream()
                .filter(s -> s.getSaleDate()
                        .equals(LocalDate.now()))
                .mapToInt(s ->
                        s.getUnitPrice()
                        * s.getSaleNumber())
                .sum();

        // 件数
        long salesCount =
                salesList.size();
        
     // 爆売れ商品
        String topProduct =
                salesRepository
                .findTopSellingProduct()
                .get(0);

        // 危険商品
        String worstProduct =
                salesRepository
                .findWorstSellingProduct()
                .get(0);
        
     // TOP3商品
        List<String> topProducts =
                salesRepository
                .findTopProducts()
                .stream()
                .limit(3)
                .collect(Collectors.toList());
        
     // 月別売上
        List<Integer> monthlySales =
                new ArrayList<>();

        for(int month = 1;
                month <= 12;
                month++) {

            final int targetMonth = month;

            int total =
                    salesList.stream()
                    .filter(s ->s.getSaleDate().getMonthValue()== targetMonth)
                    .mapToInt(s -> s.getUnitPrice() * s.getSaleNumber()).sum();
            monthlySales.add(total);
        }
        String[] messages = {

        	    "今日も一日気張ってこう！",

        	    "二言目いいっすか？",

        	    "エラーは仕様です。",

        	    "その残業、未来ありますか？",

        	    "松井くんは顔があかんわ",

        	    

        	};

        	Random random =
        	        new Random();

        	String todayMessage =
        	        messages[
        	            random.nextInt(
        	                messages.length)
        	        ];


        // モデルへ
        model.addAttribute(
                "todaySales",
                todaySales);

        model.addAttribute(
                "salesCount",
                salesCount);
        
        model.addAttribute(
                "topProduct",
                topProduct);

        model.addAttribute(
                "worstProduct",
                worstProduct);
        
        model.addAttribute(
                "topProducts",
                topProducts);
        
    	model.addAttribute(
    	        "todayMessage",
    	        todayMessage);

        
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