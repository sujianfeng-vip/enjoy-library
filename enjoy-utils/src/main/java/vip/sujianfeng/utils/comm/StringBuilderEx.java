package vip.sujianfeng.utils.comm;

import vip.sujianfeng.utils.enums.OSPlatformEnum;

public class StringBuilderEx {
	
	private StringBuilder sb;
	
	public StringBuilderEx(){
		this.sb = new StringBuilder();
	}
	
	public StringBuilderEx(String text){
		this.sb = new StringBuilder();
		this.sb.append(text);
	}	
	
	public StringBuilder getSb() {
		return sb;
	}
	
	public StringBuilderEx append(Object item){
		this.sb.append(item);
		return this;
	}	
	
	public StringBuilderEx appendRow(String item){
		this.sb.append(item);
		this.br();
		return this;
	}
	
	public StringBuilderEx appendFormater(String format, Object... args){
		this.sb.append(String.format(format, args));	
		return this;
	}
	
	public StringBuilderEx appendFormaterRow(String format, Object... args){
		this.sb.append(String.format(format, args));
		this.br();
		return this;
	}

	public StringBuilderEx appendFR(String format, Object... args){
		return appendFormaterRow(format, args);
	}
	
	public StringBuilderEx appendJsonItem(String key, String value, String endDiv){
		return appendFormaterRow("\"%s\": \"%s\"%s", key, value, endDiv);
	}	
	
	public String toString(){
		return this.sb.toString();
	}
	
	public int length(){ 
		return this.sb.length();
	}


	private StringBuilderEx br(){
		if (OSPlatformEnum.currOS() == OSPlatformEnum.currOS().Linux){
			this.sb.append("\n");
		}else if (OSPlatformEnum.currOS() == OSPlatformEnum.currOS().Mac_OS){
			this.sb.append("\r");
		}else if (OSPlatformEnum.currOS() == OSPlatformEnum.currOS().Windows){
			this.sb.append("\r\n");
		}else{
			this.sb.append("\r\n");
		}
		return this;
	}

	public void clear() {
		this.sb.setLength(0);
	}
}
