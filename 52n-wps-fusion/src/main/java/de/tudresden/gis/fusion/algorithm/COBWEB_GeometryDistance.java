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
package de.tudresden.gis.fusion.algorithm;

import java.util.HashMap;
import java.util.Map;

import org.n52.wps.algorithm.annotation.Algorithm;
import org.n52.wps.algorithm.annotation.ComplexDataInput;
import org.n52.wps.algorithm.annotation.ComplexDataOutput;
import org.n52.wps.algorithm.annotation.Execute;
import org.n52.wps.algorithm.annotation.LiteralDataInput;
import org.n52.wps.io.data.binding.literal.LiteralDoubleBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tudresden.gis.fusion.data.IData;
import de.tudresden.gis.fusion.data.binding.GTFeatureCollectionBinding;
import de.tudresden.gis.fusion.data.feature.geotools.GTFeatureCollection;
import de.tudresden.gis.fusion.data.literal.DecimalLiteral;
import de.tudresden.gis.fusion.data.relation.FeatureRelationCollection;

@Algorithm(abstrakt="Determines distance relation between input features", version="1.0")
public class COBWEB_GeometryDistance extends COBWEB_Algorithm {
	
	private static Logger LOGGER = LoggerFactory.getLogger(COBWEB_GeometryDistance.class);
	private final String NEW_ATT = "relation_" + System.currentTimeMillis();
	
	//input identifier
	private final String IN_SOURCE = "IN_SOURCE";
	private final String IN_TARGET = "IN_TARGET";
	private final String IN_THRESHOLD = "IN_THRESHOLD";
	
	//input data
	private GTFeatureCollection inReference;
	private GTFeatureCollection inTarget;
	private GTFeatureCollection outReference;
	private double inBuffer;
	
	//output identifier
	private final String OUT_SOURCE = "OUT_SOURCE";
	
	//output data
	private FeatureRelationCollection relations;

	//constructor
    public COBWEB_GeometryDistance() {
        super();
    }

    @ComplexDataInput(identifier=IN_SOURCE, title="reference features", binding=GTFeatureCollectionBinding.class, minOccurs=1, maxOccurs=1)
    public void setReference(GTFeatureCollection inReference) {
        this.inReference = inReference;
    }
    
    @ComplexDataInput(identifier=IN_TARGET, title="target features", binding=GTFeatureCollectionBinding.class, minOccurs=1, maxOccurs=1)
    public void setTarget(GTFeatureCollection inTarget) {
        this.inTarget = inTarget;
    }
    
    @LiteralDataInput(identifier=IN_THRESHOLD, title="threshold distance for relations" , binding=LiteralDoubleBinding.class, minOccurs=1, maxOccurs=1)
    public void setBuffer(double inBuffer) {
    	this.inBuffer = inBuffer;
    }
    
	@ComplexDataOutput(identifier=OUT_SOURCE, title="reference features with relations to target", binding=GTFeatureCollectionBinding.class)
    public GTFeatureCollection getReference() {
        return outReference;
    }
    
    @Execute
    public void execute() {
    	
    	LOGGER.info("Number of reference features: " + inReference.size());
    	LOGGER.info("Number of target features: " + inTarget.size());
    	LOGGER.info("Distance threshold: " + inBuffer);
    	
    	//get relations
    	Map<String,IData> input = new HashMap<String,IData>();
    	input.put(IN_SOURCE, inReference);
    	input.put(IN_TARGET, inTarget);
    	input.put(IN_THRESHOLD, new DecimalLiteral(inBuffer));
		
		Map<String,IData> output = new de.tudresden.gis.fusion.operation.measurement.GeometryDistance().execute(input);
		
		relations = (FeatureRelationCollection) output.get("OUT_RELATIONS");
		outReference = new GTFeatureCollection(inReference.identifier(), addRelations(inReference.collection(), relations, NEW_ATT));
		
    	LOGGER.info("Relation measurement returned " + relations.size() + " results");
    }

}
