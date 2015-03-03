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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tudresden.gis.fusion.data.IData;
import de.tudresden.gis.fusion.data.IFeatureRelationCollection;
import de.tudresden.gis.fusion.data.binding.GTFeatureCollectionBinding;
import de.tudresden.gis.fusion.data.geotools.GTFeatureCollection;

@Algorithm(abstrakt="Determines distance relation between input features", version="1.0")
public class COBWEB_TopologyRelation extends COBWEB_Algorithm {
	
	private static Logger LOGGER = LoggerFactory.getLogger(COBWEB_TopologyRelation.class);
	private final String NEW_ATT = "relation_" + System.currentTimeMillis();
	
	//input identifier
	private final String IN_REFERENCE = "IN_REFERENCE";
	private final String IN_TARGET = "IN_TARGET";
	
	//input data
	private GTFeatureCollection inReference;
	private GTFeatureCollection inTarget;
	private GTFeatureCollection outReference;
	
	//output identifier
	private final String OUT_REFERENCE = "OUT_REFERENCE";
	
	//output data
	private IFeatureRelationCollection relations;

	//constructor
    public COBWEB_TopologyRelation() {
        super();
    }

    @ComplexDataInput(identifier=IN_REFERENCE, title="reference features", binding=GTFeatureCollectionBinding.class, minOccurs=1, maxOccurs=1)
    public void setReference(GTFeatureCollection inReference) {
        this.inReference = inReference;
    }
    
    @ComplexDataInput(identifier=IN_TARGET, title="target features", binding=GTFeatureCollectionBinding.class, minOccurs=1, maxOccurs=1)
    public void setTarget(GTFeatureCollection inTarget) {
        this.inTarget = inTarget;
    }
    
	@ComplexDataOutput(identifier=OUT_REFERENCE, title="reference features with relations to target", binding=GTFeatureCollectionBinding.class)
    public GTFeatureCollection getReference() {
        return outReference;
    }
    
    @Execute
    public void execute() {
    	
    	LOGGER.info("Number of reference features: " + inReference.size());
    	LOGGER.info("Number of target features: " + inTarget.size());
    	
    	//get relations
    	Map<String,IData> input = new HashMap<String,IData>();
    	input.put(IN_REFERENCE, inReference);
    	input.put(IN_TARGET, inTarget);
		
		Map<String,IData> output = new de.tudresden.gis.fusion.operation.relation.TopologyRelation().execute(input);
		
		relations = (IFeatureRelationCollection) output.get("OUT_RELATIONS");
		outReference = new GTFeatureCollection(inReference.getIdentifier(), addRelations(inReference.getSimpleFeatureCollection(), relations, NEW_ATT));
		
    	LOGGER.info("Relation measurement returned " + relations.size() + " results");
    }

}
