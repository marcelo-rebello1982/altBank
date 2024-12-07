package br.com.cadastroit.services.api.services.interfaces;

public interface ReportContaService {
	
	public byte[] generateReportPdf(String nameReport);
	
	public byte[] generateReportCsv();

}