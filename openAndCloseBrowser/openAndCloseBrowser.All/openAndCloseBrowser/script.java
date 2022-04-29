import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import oracle.oats.scripting.modules.basic.api.IteratingVUserScript;
import oracle.oats.scripting.modules.basic.api.MatchOption;
import oracle.oats.scripting.modules.basic.api.ScriptService;
import oracle.oats.scripting.modules.basic.api.TextPresence;
import oracle.oats.scripting.modules.browser.api.BrowserSettings.BrowserType;
import oracle.oats.scripting.modules.functionalTest.api.PropertyTestList;
import oracle.oats.scripting.modules.webdom.api.Source;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;

public class script extends IteratingVUserScript {
	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;
	@ScriptService oracle.oats.scripting.modules.webdom.api.WebDomService web;

	public void initialize() throws Exception {
		browser.setBrowserType(BrowserType.Chrome);
		browser.launch();
		browser.clearBrowser();
	}

	/**
	 * Add code to be executed each iteration for this virtual user.
	 */
	public void run() throws Exception {
		/** Step1: enter application URL */
		web.window(
			"/web:window[@index='0' or @title='https://www.mytoys.de']").navigate(
			"https://www.mytoys.de/");
		/* wait for page load */
		web.window(
			"/web:window[@index='0' or @title='https://www.mytoys.de']").waitForPage(
			100,
			true);
		/* get the title of the laanding page */
		String title = web.window(
			"/web:window[@index='0' or @title='https://www.mytoys.de']").getTitle();
		/* verify the landing page */
		web.verifyText(
			"myToys Online Shop | Einfach alles für Ihr Kind",
			title,
			Source.DisplayContent,
			TextPresence.PassIfPresent,
			MatchOption.Exact);

		/** Step2: type for 'trampoline' in the search input field */
		web.textBox(
			5,
			"/web:window[@index='0' or @title='myToys Online Shop | Einfach alles für Ihr Kind']/web:document[@index='0']/web:form[@index='0']/web:input_search[@index='0']").click();
		{
			think(1.289);
		}
		web.textBox(
			6,
			"/web:window[@index='0' or @title='myToys Online Shop | Einfach alles für Ihr Kind']/web:document[@index='0']/web:form[@index='0']/web:input_search[@index='0']").setText(
			"trampoline");
		{
			think(2.324);
		}
		/* click on search icon */
		web.button(
			7,
			"/web:window[@index='0' or @title='myToys Online Shop | Einfach alles für Ihr Kind']/web:document[@index='0']/web:form[@index='0']/web:button[@index='2']").click();

		/** Step3: select 'Höchster Preis' from dropdown */
		{
			think(5.873);
		}
		web.selectBox(
			10,
			"/web:window[@index='0' or @title='trampoline | myToys']/web:document[@index='0']/web:select[(@name='select' or @index='0') and multiple mod 'false']").selectOptionByText(
			"Höchster Preis");
		{
			think(5.873);
		}
		/** Step4: verify search result message */
		String searchedResult=web.element(
			"/web:window[@index='0']/web:document[@index='0']/web:h1[@class='pop-main__title' or @index='0']").getDisplayText();
		info("Text displayed"+searchedResult);
		web.assertText(
			"Suchergebnis für \"trampoline\"",
			searchedResult,
			Source.DisplayContent,
			TextPresence.PassIfPresent,
			MatchOption.Exact);		
		
		
		/** Step5: Identify the common parent */
		PropertyTestList ptList = new PropertyTestList();
		ptList.add(
			"classname",
			"prod-tile__price-container");
		/* get the list of products */
		List<DOMElement> eles = web.document(
			"/web:window[ @title='trampoline | myToys']/web:document[@index='0']").getElementsByTagName(
			"div",
			ptList);

		for (int i = 0; i < eles.size(); i++) {
			if (eles.get(
				i).getChildren().size() == 1) {

				Number amount = NumberFormat.getNumberInstance(
					Locale.GERMAN).parse(
					eles.get(
						i).getDisplayText().replace(
						" €",
						""));
				info("Content:" + amount);
				if (amount.doubleValue() < 1000) {
					eles.get(
						i).click();
					break;
				}

			} else if (eles.get(
				i).getChildren().size() == 2) {
				Number amount = NumberFormat.getNumberInstance(
					Locale.GERMAN).parse(
					eles.get(
						i).getChildren().get(
						1).getDisplayText().replace(
						" €",
						""));
				info("Content:" + amount);
				if (amount.doubleValue() < 1000) {
					eles.get(
						i).click();
					break;
				}
			} else if (eles.get(
				i).getChildren().size() == 3) {

				Number amount = NumberFormat.getNumberInstance(
					Locale.GERMAN).parse(
					eles.get(
						i).getChildren().get(
						2).getDisplayText().replace(
						" €",
						""));
				info("Content:" + amount);
				if (amount.doubleValue() < 1000) {
					eles.get(
						i).click();
					break;
				}
			}
		}
		{
			think(2.53);
		}
		/** Step6: click on 'In den Warenkorb' */
		web.button(
			15,
			"/web:window[@index='0']/web:document[@index='0']/web:span[@text='In den Warenkorb']").click();
		{
			think(7.168);
		}
		info("sucess message: ");
		/** Step7: get sucess message */
		String sucessMsg = web.element(
			16,
			"/web:window[@index='0']/web:document[@index='0']/web:div[@class='msg-success js-msg-add' or @index='390']").getDisplayText();
		info("sucess message: " + sucessMsg);
		/* validate the sucess message */
		web.assertText(
			"Der Artikel wurde Ihrem Warenkorb hinzugefügt.",
			sucessMsg,
			Source.DisplayContent,
			TextPresence.PassIfPresent,
			MatchOption.Exact);
		/* validate the sucess icon */
		String sucessIcon = Boolean.toString(web.element(
			"/web:window[@index='0']/web:document[@index='0']/web:div[@class='msg-success-icon js-msg-add' or @index='389']").isDisplayed());
		web.assertText(
			"true",
			sucessIcon,
			Source.DisplayContent,
			TextPresence.PassIfPresent,
			MatchOption.Exact);
		/* close popup */
		web.button(
			"/web:window[@index='0' or @title='Freebound Trampolin &quot;BOWL&quot;, plum | myToys']/web:document[@index='0']/web:button[@text='Overlay schließen' or @index='30']").click();

	}

	public void finish() throws Exception {
		
		browser.closeAllBrowsers();
		
	}
}
