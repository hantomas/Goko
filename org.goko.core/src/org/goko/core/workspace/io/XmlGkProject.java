/**
 *
 */
package org.goko.core.workspace.io;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;

/**
 * @author PsyKo
 * @date 9 d�c. 2015
 */
public class XmlGkProject {

//	@ElementList
	private ArrayList<XmlProjectContainer> lstProjectContainer;

	@ElementList
	private ArrayList<Object> testList;

	public XmlGkProject() {
		super();
		this.lstProjectContainer = new ArrayList<>();
		this.testList =  new ArrayList<>();
	}

	/**
	 * @return the lstProjectContainer
	 */
	public ArrayList<XmlProjectContainer> getLstProjectContainer() {
		return lstProjectContainer;
	}

	/**
	 * @param lstProjectContainer the lstProjectContainer to set
	 */
	public void setLstProjectContainer(ArrayList<XmlProjectContainer> lstProjectContainer) {
		this.lstProjectContainer = lstProjectContainer;
	}

	/**
	 * @return the testList
	 */
	public ArrayList<Object> getTestList() {
		return testList;
	}

	/**
	 * @param testList the testList to set
	 */
	public void setTestList(ArrayList<Object> testList) {
		this.testList = testList;
	}

}
