/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.report.manager.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ewcms.plugin.BaseException;
import com.ewcms.plugin.report.manager.dao.ChartReportDAO;
import com.ewcms.plugin.report.manager.util.ChartAnalysisUtil;
import com.ewcms.plugin.report.manager.util.ParameterSetValueUtil;
import com.ewcms.plugin.report.model.ChartReport;
import com.ewcms.plugin.report.model.Parameter;

/**
 * 
 * @author wu_zhijun
 *
 */
@Service
public class ChartReportService implements ChartReportServiceable {

	@Autowired
	private ChartReportDAO chartReportDAO;
	
	@Override
	public Long addChartReport(ChartReport chartReport){
		Assert.notNull(chartReport);
		Assert.hasLength(chartReport.getChartSql());

		Set<Parameter> parameters = ChartAnalysisUtil.analysisSql(chartReport.getChartSql());
		chartReport.setParameters(parameters);

		chartReportDAO.persist(chartReport);
		return chartReport.getId();
	}

	@Override
	public Long updChartReport(ChartReport chartReport){
		Assert.notNull(chartReport);
		Assert.hasLength(chartReport.getChartSql());
		
		ChartReport entity = chartReportDAO.get(chartReport.getId());
		
		entity.setName(chartReport.getName());
		entity.setBaseDS(chartReport.getBaseDS());
		entity.setChartType(chartReport.getChartType());
		entity.setShowTooltips(chartReport.getShowTooltips());
		entity.setChartTitle(chartReport.getChartTitle());
		entity.setFontName(chartReport.getFontName());
		entity.setFontSize(chartReport.getFontSize());
		entity.setFontStyle(chartReport.getFontStyle());
		entity.setHorizAxisLabel(chartReport.getHorizAxisLabel());
		entity.setVertAxisLabel(chartReport.getVertAxisLabel());
		entity.setDataFontName(chartReport.getDataFontName());
		entity.setDataFontSize(chartReport.getDataFontSize());
		entity.setDataFontStyle(chartReport.getDataFontStyle());
		entity.setAxisFontName(chartReport.getAxisFontName());
		entity.setAxisFontSize(chartReport.getAxisFontSize());
		entity.setAxisFontStyle(chartReport.getAxisFontStyle());
		entity.setAxisTickFontName(chartReport.getAxisTickFontName());
		entity.setAxisTickFontSize(chartReport.getAxisTickFontSize());
		entity.setAxisTickFontStyle(chartReport.getAxisTickFontStyle());
		entity.setTickLabelRotate(chartReport.getTickLabelRotate());
		entity.setShowLegend(chartReport.getShowLegend());
		entity.setLegendPosition(chartReport.getLegendPosition());
		entity.setLegendFontName(chartReport.getLegendFontName());
		entity.setLegendFontSize(chartReport.getLegendFontSize());
		entity.setLegendFontStyle(chartReport.getLegendFontStyle());
		entity.setChartHeight(chartReport.getChartHeight());
		entity.setChartWidth(chartReport.getChartWidth());
		entity.setBgColorB(chartReport.getBgColorB());
		entity.setBgColorG(chartReport.getBgColorG());
		entity.setBgColorR(chartReport.getBgColorR());
		entity.setRemarks(chartReport.getRemarks());
		
		if (!entity.getChartSql().equals(chartReport.getChartSql())) {
			entity.setChartSql(chartReport.getChartSql());
			Set<Parameter> icNewList = new LinkedHashSet<Parameter>();
			
			Set<Parameter> oldParameters = entity.getParameters();
			Set<Parameter> newParameters = ChartAnalysisUtil.analysisSql(chartReport.getChartSql());
			for (Parameter newParameter : newParameters){
				Parameter ic = findListEntity(oldParameters,newParameter);
				if (ic == null){
					ic = newParameter;
				}
				icNewList.add(ic);
			}
			entity.setParameters(icNewList);
		}
		chartReportDAO.merge(entity);
		return chartReport.getId();
	}

	@Override
	public void delChartReport(Long chartReportId){
		chartReportDAO.removeByPK(chartReportId);
	}

	@Override
	public ChartReport findChartReportById(Long chartReportId){
		return chartReportDAO.get(chartReportId);
	}

	@Override
	public List<ChartReport> findAllChartReport() {
		return chartReportDAO.findAll();
	}
	
	@Override
	public Long updChartReportParameter(Long chartReportId, Parameter parameter) throws BaseException {
		if (chartReportId == null || chartReportId.intValue() == 0)
			throw new BaseException("", "图型编号不存在，请重新选择！");
		ChartReport chart = chartReportDAO.get(chartReportId);
		if (chart == null)
			throw new BaseException("", "图型不存在，请重新选择！");
		
		parameter = ParameterSetValueUtil.setParametersValue(parameter);
		
		Set<Parameter> parameters = chart.getParameters();
		parameters.remove(parameter);
		parameters.add(parameter);
		chart.setParameters(parameters);
		
		chartReportDAO.merge(chart);	
		
		return parameter.getId();
	}
	
	/**
	 * 根据参数名查询数据库中的参数集合
	 * 
	 * @param oldParameters
	 *            数据库中的报表参数集合
	 * @param newParameter 新参数
	 *            
	 * @return Parameter
	 */
	private Parameter findListEntity(Set<Parameter> oldParameters, Parameter newParameter) {
		for (Parameter parameter : oldParameters) {
			String rpEnName = parameter.getEnName();
			if (newParameter.getEnName().trim().equals(rpEnName.trim())) {
				parameter.setClassName(newParameter.getClassName());
				parameter.setDefaultValue(newParameter.getDefaultValue());
				parameter.setDescription(newParameter.getDescription());
				return parameter;
			}
		}
		return null;
	}
}
