/**
 * 
 */
package com.tenpay.sm.web.plugin;

/**
 * 页面中生成script的plugin
 * @author li.hongtl
 *
 */
public class ScriptPlugin implements SmPlugin {
	private String script;
	private String language = "javascript";
	public ScriptPlugin() {
	}
	public ScriptPlugin(String script) {
		this.script = script;
	}
	public ScriptPlugin(String script,String language) {
		this.script = script;
		this.language = language;
	}
	/* (non-Javadoc)
	 * @see com.tenpay.sm.web.tag.SmPlugin#generateContent()
	 */
	public String generateContent() {
		String str = new java.util.Formatter().format(
				"<script language='%s'>%s</script>",
				language,script).toString();
		return str;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
	
	
}
