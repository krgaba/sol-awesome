package com.sol.awesome.nomination.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name", scope = SolsticePrinciple.class)
public enum SolsticePrinciple {
	WorkHard("Encourages working"), SleepLess("Promotes efficiency");
	
	
	private String description;

	private SolsticePrinciple(String description) {
		this.description = description;
	}
	
	public String toString() {
		return description;
	}
	

}
