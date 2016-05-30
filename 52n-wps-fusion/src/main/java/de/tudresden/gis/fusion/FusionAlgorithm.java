/**
 * ﻿Copyright (C) 2014 - 2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *       • Apache License, version 2.0
 *       • Apache Software License, version 1.0
 *       • GNU Lesser General Public License, version 3
 *       • Mozilla Public License, versions 1.0, 1.1 and 2.0
 *       • Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package de.tudresden.gis.fusion;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.opengis.wps.x100.ProcessDescriptionType;

import org.n52.wps.io.data.IData;
import org.n52.wps.server.AbstractSelfDescribingAlgorithm;
import org.n52.wps.server.ExceptionReport;

import de.tudresden.gis.fusion.operation.IOperation;
import de.tudresden.gis.fusion.operation.constraint.IDataConstraint;
import de.tudresden.gis.fusion.operation.constraint.MandatoryConstraint;
import de.tudresden.gis.fusion.operation.description.IInputDescription;
import de.tudresden.gis.fusion.operation.description.IOutputDescription;

public class FusionAlgorithm extends AbstractSelfDescribingAlgorithm {
	
	IOperation fusionOperation;
	
	public FusionAlgorithm(IOperation fusionOperation){
		this.fusionOperation = fusionOperation;
	}

	@Override
	public Map<String,IData> run(Map<String, List<IData>> inputData) throws ExceptionReport {
		
		Map<String,de.tudresden.gis.fusion.data.IData> fusionInputs = new HashMap<String,de.tudresden.gis.fusion.data.IData>();
		for(Map.Entry<String,List<IData>> input : inputData.entrySet()){
			fusionInputs.put(input.getKey(), DataTransformer.transformIData(input.getValue()));
		}
		Map<String, IData> resultData = new HashMap<String,IData>();
		Map<String,de.tudresden.gis.fusion.data.IData> fusionResults = this.fusionOperation.execute(fusionInputs);
		for(Map.Entry<String,de.tudresden.gis.fusion.data.IData> result : fusionResults.entrySet()){
			resultData.put(result.getKey(), DataTransformer.transformIData(result.getValue()));
		}
		
		return resultData;
	}

	@Override
	public Class<?> getInputDataType(String id) {
		Collection<IInputDescription> descriptions = fusionOperation.profile().inputDescriptions();
		for(IInputDescription description : descriptions){
			if(description.identifier().equals(id))
				return DataTransformer.getSupportedClass(description);
		}
		return null;
	}

	@Override
	public Class<?> getOutputDataType(String id) {
		Collection<IOutputDescription> descriptions = fusionOperation.profile().outputDescriptions();
		for(IOutputDescription description : descriptions){
			if(description.identifier().equals(id))
				return DataTransformer.getSupportedClass(description);
		}
		return null;
	}

	@Override
	public List<String> getInputIdentifiers() {
		Collection<IInputDescription> descriptions = fusionOperation.profile().inputDescriptions();
		List<String> identifiers = new ArrayList<String>();
		for(IInputDescription description : descriptions){
			identifiers.add(description.identifier());
		}
		return identifiers;
	}

	@Override
	public List<String> getOutputIdentifiers() {
		Collection<IOutputDescription> descriptions = fusionOperation.profile().outputDescriptions();
		List<String> identifiers = new ArrayList<String>();
		for(IOutputDescription description : descriptions){
			identifiers.add(description.identifier());
		}
		return identifiers;
	}
	
	@Override
	public String getWellKnownName() {
		return "de.tudresden.gis.fusion.algorithm." + this.fusionOperation.profile().processDescription().identifier();
	}
	
	@Override
	protected ProcessDescriptionType initializeDescription() {
		ProcessDescriptionType type = super.initializeDescription();
		type.addNewAbstract().setStringValue(getAbstract());
		return type;
	}
	
	public String getAbstract() {
		return this.fusionOperation.profile().processDescription().getDescription();
	}
	
	@Override
	public BigInteger getMinOccurs(String id){
		Collection<IInputDescription> ioDesc = fusionOperation.profile().inputDescriptions();
		for(IInputDescription io : ioDesc){
			if(io.identifier().equalsIgnoreCase(id) && isMandatory(io.constraints()))
				return new BigInteger("1");
		}
		return new BigInteger("0");
	}
	
	private boolean isMandatory(Collection<IDataConstraint> constraints) {
		for(IDataConstraint constraint : constraints){
			if(constraint instanceof MandatoryConstraint)
				return true;
		}
		return false;
	}

	@Override
	public BigInteger getMaxOccurs(String identifier){
		return new BigInteger("1");
	}

}
