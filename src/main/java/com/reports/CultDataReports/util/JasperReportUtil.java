package com.reports.CultDataReports.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class JasperReportUtil {

    public static byte[] exportToExcel(String jasperFilePath, List<?> dataList, Map<String, Object> parameters) throws JRException {
        InputStream reportTemplate = JasperReportUtil.class.getResourceAsStream(jasperFilePath);
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportTemplate);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));

        SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
        config.setOnePagePerSheet(false);
        config.setDetectCellType(true);
        config.setRemoveEmptySpaceBetweenColumns(true);
        config.setRemoveEmptySpaceBetweenRows(true);
        config.setWhitePageBackground(false);
        config.setIgnoreGraphics(true);
        config.setWrapText(true);
        config.setCollapseRowSpan(false);

        exporter.setConfiguration(config);
        exporter.exportReport();

        return xlsReport.toByteArray();
    }
}
