package com.example.demo.dto.RH;

import lombok.Data;
import java.util.*;
import java.util.Map;
@Data
public class StatistiqueDTO {
    private int annee;

    // Totaux mensuels pour janvier à décembre
    private List<Double> totalSalaire;   // Total des salaires par mois
    private List<Double> totalDeduction; // Total des déductions par mois
    private List<Double> totalEarning;   // Total des gains par mois

    // Totaux annuels
    private Double totalAnnuelsalaire;   // Total annuel des salaires
    private Double totalAnnuelDeduction; // Total annuel des déductions
    private Double totalAnnuelEarning;   // Total annuel des gains

    // Association entre mois (1-12) et la liste des fiches de salaire de ce mois
    private Map<Integer, List<SalarySlipDTO>> salarySlips;

    public Double getsum(List<Double> values) {
        if (values == null) return 0.0;
        return values.stream()
            .filter(v -> v != null)
            .mapToDouble(Double::doubleValue)
            .sum();
    }

    public void setSalarySlips(Map<Integer, List<SalarySlipDTO>> salarySlips) {
        this.salarySlips = salarySlips;

        // Initialiser les listes mensuelles avec 12 zéros (janvier à décembre)
        totalSalaire = new ArrayList<>();
        totalDeduction = new ArrayList<>();
        totalEarning = new ArrayList<>();

        for (int mois = 1; mois <= 12; mois++) {
            List<SalarySlipDTO> slips = salarySlips.get(mois);

            if (slips == null || slips.isEmpty()) {
                totalSalaire.add(0.0);
                totalDeduction.add(0.0);
                totalEarning.add(0.0);
            } else {
                double sumSalaire = slips.stream()
                    .mapToDouble(SalarySlipDTO::getNet_pay)
                    .sum();

                double sumDeduction = slips.stream()
                    .mapToDouble(SalarySlipDTO::getTotal_deduction)
                    .sum();

                double sumEarning = slips.stream()
                    .mapToDouble(SalarySlipDTO::getTotal_earnings)
                    .sum();

                totalSalaire.add(sumSalaire);
                totalDeduction.add(sumDeduction);
                totalEarning.add(sumEarning);
            }
        }

        // Calculer les totaux annuels en sommant les listes mensuelles
        totalAnnuelsalaire = getsum(totalSalaire);
        totalAnnuelDeduction = getsum(totalDeduction);
        totalAnnuelEarning = getsum(totalEarning);
    }
}
