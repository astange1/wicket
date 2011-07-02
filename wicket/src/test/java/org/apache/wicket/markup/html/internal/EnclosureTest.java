/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.markup.html.internal;

import java.io.IOException;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.resource.DummyApplication;
import org.apache.wicket.util.diff.DiffUtil;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;


/**
 * 
 * @author Juergen Donnerstag
 */
public class EnclosureTest extends WicketTestCase
{
	/**
	 * Construct.
	 * 
	 * @param name
	 */
	public EnclosureTest(final String name)
	{
		super(name);
	}

	/**
	 * 
	 * @see org.apache.wicket.WicketTestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception
	{
		WebApplication app = new DummyApplication();
		tester = new WicketTester(app);
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage() throws Exception
	{
		executeTest(EnclosurePage_1.class, "EnclosurePageExpectedResult_1.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage2() throws Exception
	{
		executeTest(EnclosurePage_2.class, "EnclosurePageExpectedResult_2.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage3() throws Exception
	{
		executeTest(EnclosurePage_3.class, "EnclosurePageExpectedResult_3.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage4() throws Exception
	{
		executeTest(EnclosurePage_4.class, new PageParameters("visible=false"),
			"EnclosurePageExpectedResult_4.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage4_1() throws Exception
	{
		executeTest(EnclosurePage_4.class, new PageParameters("visible=true"),
			"EnclosurePageExpectedResult_4-1.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage5() throws Exception
	{
		executeTest(EnclosurePage_5.class, new PageParameters("visible=false"),
			"EnclosurePageExpectedResult_5.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage5_1() throws Exception
	{
		executeTest(EnclosurePage_5.class, new PageParameters("visible=true"),
			"EnclosurePageExpectedResult_5-1.html");
	}

	/**
	 * Tests visibility of children after enclosure has been made hidden and visible again
	 * 
	 * @throws Exception
	 */
	public void testVisibilityOfChildren() throws Exception
	{
		// render with enclosure initally visible
		tester.startPage(EnclosurePage_6.class);
		String doc = tester.getServletResponse().getDocument();
		assertTrue(doc.contains("content1"));
		assertTrue(doc.contains("content2"));

		// render with enclosure hidden
		tester.clickLink("link");
		doc = tester.getServletResponse().getDocument();
		assertFalse(doc.contains("content1"));
		assertFalse(doc.contains("content2"));

		// render with enclosure visible again
		tester.clickLink("link");
		doc = tester.getServletResponse().getDocument();
		assertTrue(doc.contains("content1"));
		assertTrue(doc.contains("content2"));
	}

	/**
	 * 
	 */
	public void testRender()
	{
		tester = new WicketTester(EnclosurePage_7.class);
		tester.startPage(EnclosurePage_7.class);
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void testRender8() throws Exception
	{
		executeTest(EnclosurePage_8.class, "EnclosurePageExpectedResult_8.html");
	}

	/**
	 * 
	 * @param page
	 * @param file
	 * @throws Exception
	 */
	private void executePage(final EnclosurePage_9 page, final String file) throws Exception
	{
		page.reset();
		tester.startPage(page);
		tester.assertRenderedPage(page.getClass());
		assertResultPage(file);
	}

	private void assertResultPage(final String file) throws IOException
	{
		String document = tester.getServletResponse().getDocument();
		document = document.replaceAll(":form:[1-90]+:IFormSubmitListener:",
			":form::IFormSubmitListener:");
		DiffUtil.validatePage(document, getClass(), file, true);
	}

	/**
	 * @throws Exception
	 */
	public void testRender9() throws Exception
	{
		Class<? extends Page> clazz = EnclosurePage_9.class;
		tester = new WicketTester(clazz);

		executeTest(clazz, "EnclosurePageExpectedResult_9.html");
		EnclosurePage_9 page = (EnclosurePage_9)tester.getLastRenderedPage();
		assertTrue(page.inputOnBeforeRender);
		assertFalse(page.inputValidate);
		assertTrue(page.labelOnBeforeRender);

		page.reset();
		page.get("form:label").setVisible(false);
		executePage(page, "EnclosurePageExpectedResult_9-2.html");
		// It should be FALSE, but because of auto-component etc. it doesn't
		// assertFalse(page.inputOnBeforeRender);
		assertTrue(page.inputOnBeforeRender);
		assertFalse(page.inputValidate);
		assertFalse(page.labelOnBeforeRender);

		page.reset();
		page.get("form:label").setVisible(true);
		executePage(page, "EnclosurePageExpectedResult_9.html");
		assertTrue(page.inputOnBeforeRender);
		assertFalse(page.inputValidate);
		assertTrue(page.labelOnBeforeRender);

		page.reset();
		page.get("form:input").setVisible(false);
		executePage(page, "EnclosurePageExpectedResult_9-3.html");
		assertFalse(page.inputOnBeforeRender);
		assertFalse(page.inputValidate);
		assertTrue(page.labelOnBeforeRender);

		page.reset();
		page.get("form:label").setVisible(false);
		executePage(page, "EnclosurePageExpectedResult_9-2.html");
		assertFalse(page.inputOnBeforeRender);
		assertFalse(page.inputValidate);
		assertFalse(page.labelOnBeforeRender);

		page.reset();
		page.get("form:label").setVisible(true);
		executePage(page, "EnclosurePageExpectedResult_9-3.html");
		assertFalse(page.inputOnBeforeRender);
		assertFalse(page.inputValidate);
		assertTrue(page.labelOnBeforeRender);

		page.reset();
		page.get("form:input").setVisible(true);
		executePage(page, "EnclosurePageExpectedResult_9.html");
		assertTrue(page.inputOnBeforeRender);
		assertFalse(page.inputValidate);
		assertTrue(page.labelOnBeforeRender);
	}

	/**
	 * @throws Exception
	 */
	public void testRender9a() throws Exception
	{
		Class<? extends Page> clazz = EnclosurePage_9.class;
		tester = new WicketTester(clazz);

		executeTest(clazz, "EnclosurePageExpectedResult_9.html");
		EnclosurePage_9 page = (EnclosurePage_9)tester.getLastRenderedPage();

		page.reset();
		FormTester formTester = tester.newFormTester("form");
		tester.getServletRequest().setParameter(((CheckBox)page.get("form:input")).getInputName(),
			"true");
		page.get("form:label").setVisible(true);
		formTester.submit();
		tester.assertRenderedPage(clazz);
		assertResultPage("EnclosurePageExpectedResult_9-4.html");
		assertTrue(page.inputOnBeforeRender);
		assertTrue(page.inputValidate);
		assertTrue(page.labelOnBeforeRender);

		page.reset();
		tester.getServletRequest().setParameter(((CheckBox)page.get("form:input")).getInputName(),
			"true");
		page.get("form:label").setVisible(false);
		tester.submitForm("form");
		tester.assertRenderedPage(clazz);
		assertResultPage("EnclosurePageExpectedResult_9-2.html");
		// It should be FALSE, but because of auto-component etc. it doesn't
		// assertFalse(page.inputOnBeforeRender);
		assertTrue(page.inputOnBeforeRender);
		// It should be FALSE, but because of auto-component etc. it doesn't
		// assertFalse(page.inputValidate);
		assertTrue(page.inputValidate);
		assertFalse(page.labelOnBeforeRender);
	}

	/**
	 * It must not be a difference if the enclosure controller child is a FormComponent.
	 * 
	 * @throws Exception
	 */
	public void testRender10() throws Exception
	{
		Class<? extends Page> clazz = EnclosurePage_10.class;
		executeTest(clazz, "EnclosurePageExpectedResult_10.html");

		Page page = tester.getLastRenderedPage();
		page.get("input").setVisible(false);
		tester.startPage(page);
		tester.assertRenderedPage(clazz);
		tester.assertResultPage(getClass(), "EnclosurePageExpectedResult_10-2.html");

		page.get("input").setVisible(true);
		tester.startPage(page);
		tester.assertRenderedPage(clazz);
		tester.assertResultPage(getClass(), "EnclosurePageExpectedResult_10.html");

		page.get("label").setVisible(false);
		tester.startPage(page);
		tester.assertRenderedPage(clazz);
		tester.assertResultPage(getClass(), "EnclosurePageExpectedResult_10-3.html");

		page.get("input").setVisible(false);
		tester.startPage(page);
		tester.assertRenderedPage(clazz);
		tester.assertResultPage(getClass(), "EnclosurePageExpectedResult_10-2.html");

		page.get("input").setVisible(true);
		tester.startPage(page);
		tester.assertRenderedPage(clazz);
		tester.assertResultPage(getClass(), "EnclosurePageExpectedResult_10-3.html");

		page.get("label").setVisible(true);
		tester.startPage(page);
		tester.assertRenderedPage(clazz);
		tester.assertResultPage(getClass(), "EnclosurePageExpectedResult_10.html");
	}

	/**
	 * @throws Exception
	 */
	public void testRenderPage11() throws Exception
	{
		executeTest(EnclosurePage_11.class, "EnclosurePageExpectedResult_11.html");
	}

	public void testBadEnclosure1()
	{
		class TestPage extends WebPage implements IMarkupResourceStreamProvider
		{
			public TestPage()
			{
				add(new WebMarkupContainer("d"));
				add(new Label("msg", "hi"));
			}

			public IResourceStream getMarkupResourceStream(MarkupContainer container,
				Class<?> containerClass)
			{
				return new StringResourceStream(
					"<html><body><div wicket:id='d'><div wicket:enclosure='msg'><span wicket:id='msg'></span></div></div></body></html>");
			}
		}

		try
		{
			tester.startPage(new TestPage());
		}
		catch (WicketRuntimeException e)
		{
			assertTrue(e.getMessage().startsWith("Could not find child"));
		}
	}
}
