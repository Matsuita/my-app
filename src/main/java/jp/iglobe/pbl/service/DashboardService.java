package jp.iglobe.pbl.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jp.iglobe.pbl.model.sales.Sale;
import jp.iglobe.pbl.repository.SalesRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final int
        TARGET_SALES = 30000;

    private final SalesRepository
        salesRepository;

    // 今日売上
    public long getTodaySales() {

        List<Sale> salesList =
            salesRepository.findAll();

        return salesList.stream()
            .filter(s ->
                s.getSaleDate()
                .equals(LocalDate.now()))
            .mapToLong(s ->
                s.getUnitPrice().longValue()
                * s.getSaleNumber().longValue())
            .sum();
    }

    // 売上件数
    public long getSalesCount() {

        return salesRepository
            .findAll()
            .size();
    }

    // 売れてる商品
    public String getTopProduct() {

        List<String> topProductList =
            salesRepository
            .findTopSellingProduct();

        return topProductList.isEmpty()
            ? "データなし"
            : topProductList.get(0);
    }

    // 売れてない商品
    public String getWorstProduct() {

        List<String> worstProductList =
            salesRepository
            .findWorstSellingProduct();

        return worstProductList.isEmpty()
            ? "データなし"
            : worstProductList.get(0);
    }

    // TOP3
    public List<String> getTopProducts() {

        return salesRepository
            .findTopProducts()
            .stream()
            .limit(3)
            .collect(Collectors.toList());
    }

    // 月別売上
    public List<Integer> getMonthlySales() {

        List<Sale> salesList =
            salesRepository.findAll();

        List<Integer> monthlySales =
            new ArrayList<>();

        for(int month = 1;
                month <= 12;
                month++) {

            final int targetMonth =
                month;

            int total =
                salesList.stream()
                .filter(s ->
                    s.getSaleDate()
                    .getMonthValue()
                    == targetMonth)
                .mapToInt(s ->
                    s.getUnitPrice()
                    * s.getSaleNumber())
                .sum();

            monthlySales.add(total);
        }

        return monthlySales;
    }

    // 今日の一言
    public String getTodayMessage() {

        String[] messages = {

            "今日も一日頑張りましょう！",

            "まだ舞える",

            "エラーは仕様です。",

            "売って売って売りまくれ",

            "やる気で何とかしろ",

            "ゾス！！！！！！！！！"
        };

        Random random =
            new Random();

        return messages[
            random.nextInt(
                messages.length)];
    }

    // 達成率
    public long getAchievementRate(
            long todaySales) {

        long achievementRate =
            todaySales * 100
            / TARGET_SALES;

        if(achievementRate > 100) {

            achievementRate = 100;
        }

        return achievementRate;
    }

    // ランク
    public String getSalesRank(
            long achievementRate) {

        if(achievementRate >= 90) {

            return "S";

        } else if(achievementRate >= 70) {

            return "A";

        } else if(achievementRate >= 50) {

            return "B";

        } else if(achievementRate >= 30) {

            return "C";
        }

        return "D";
    }

    // コメント
    public String getSalesComment(
            long achievementRate) {

        if(achievementRate >= 90) {

            return "かなり好調です。";

        } else if(achievementRate >= 70) {

            return "好調です。";

        } else if(achievementRate >= 50) {

            return "順調に売れています。";

        } else if(achievementRate >= 30) {

            return "まだ伸びしろがあります。";
        }

        return "ここから巻き返しです。";
    }
}