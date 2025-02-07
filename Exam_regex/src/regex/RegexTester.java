package regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegexTester {

	public static List<Boolean> test(Regex regex, String[] strings) {
		Pattern regx = Pattern.compile(regex.getPattern());
		List<Boolean> sami = new ArrayList<>();
		for (int i = 0; i < strings.length; i++) {
			sami.add(regx.matcher(strings[i]).matches());
			
		}
		return sami;
	}
}
