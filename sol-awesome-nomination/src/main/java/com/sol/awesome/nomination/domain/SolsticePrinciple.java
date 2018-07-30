package com.sol.awesome.nomination.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name", scope = SolsticePrinciple.class)
public enum SolsticePrinciple {
	ServantLeadership("Servant Leadership"), PracticeEmpathy("Practice Empathy"),
	EmpowerDisruption("Empower Disruption"), CatchExcellence("Catch Excellence"),
	MakeADifference("Make A difference");
	
	
	private String description;

	private SolsticePrinciple(String description) {
		this.description = description;
	}
	
	public String toString() {
		return description;
	}
	

}
