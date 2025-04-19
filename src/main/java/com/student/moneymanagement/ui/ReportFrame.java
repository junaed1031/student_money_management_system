package com.student.moneymanagement.ui;

import com.student.moneymanagement.dao.ExpenseDAO;
import com.student.moneymanagement.dao.IncomeDAO;
import com.student.moneymanagement.model.Expense;
import com.student.moneymanagement.model.Income;
import com.student.moneymanagement.model.User;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ReportFrame extends JFrame {
    private User user;
    private IncomeDAO incomeDAO;
    private ExpenseDAO expenseDAO;
    private JTextArea reportTextArea;

    public ReportFrame(User user) {
        this.user = user;
        this.incomeDAO = new IncomeDAO();
        this.expenseDAO = new ExpenseDAO();

        setTitle("Financial Reports");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton monthlyReportButton = new JButton("Monthly Report");
        JButton quarterlyReportButton = new JButton("Quarterly Report");
        JButton yearlyReportButton = new JButton("Yearly Report");
        JButton exportToPdfButton = new JButton("Export to PDF");

        buttonPanel.add(monthlyReportButton);
        buttonPanel.add(quarterlyReportButton);
        buttonPanel.add(yearlyReportButton);
        buttonPanel.add(exportToPdfButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        monthlyReportButton.addActionListener(e -> generateReport("monthly"));
        quarterlyReportButton.addActionListener(e -> generateReport("quarterly"));
        yearlyReportButton.addActionListener(e -> generateReport("yearly"));
        exportToPdfButton.addActionListener(e -> exportReportToPDF());
    }

    private void generateReport(String type) {
        StringBuilder report = new StringBuilder();
        report.append("Financial Report for ").append(user.getUsername()).append("\n\n");

        List<Income> incomes = incomeDAO.getIncomeByUserId(user.getUserId());
        List<Expense> expenses = expenseDAO.getExpensesByUserId(user.getUserId());

        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();

        report.append("----- ").append(type.toUpperCase()).append(" REPORT -----\n");
        report.append("Total Income: $").append(totalIncome).append("\n");
        report.append("Total Expenses: $").append(totalExpenses).append("\n");
        report.append("Net Savings: $").append(totalIncome - totalExpenses).append("\n\n");

        // Analyze spending habits
        report.append("Spending Habits:\n");
        expenses.stream()
                .collect(java.util.stream.Collectors.groupingBy(Expense::getCategory, java.util.stream.Collectors.summingDouble(Expense::getAmount)))
                .forEach((category, amount) -> report.append(" - ").append(category).append(": $").append(amount).append("\n"));

        reportTextArea.setText(report.toString());
    }

    private void exportReportToPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report as PDF");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".pdf";

            try (PdfWriter writer = new PdfWriter(filePath);
                 PdfDocument pdfDoc = new PdfDocument(writer);
                 Document document = new Document(pdfDoc)) {

                document.add(new Paragraph("Financial Report for " + user.getUsername()));
                document.add(new Paragraph(reportTextArea.getText()));

                JOptionPane.showMessageDialog(this, "Report saved successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save report: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // Dummy user for testing
        User testUser = new User();
        testUser.setUserId(1);
        testUser.setUsername("TestUser");

        SwingUtilities.invokeLater(() -> new ReportFrame(testUser).setVisible(true));
    }
}