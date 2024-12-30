package com.winterhavenmc.util.messagebuilder.messages;


import com.winterhavenmc.util.messagebuilder.macro.MacroKey;
import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;

public enum Macro implements MacroKey {

	DURATION(ProcessorType.NUMBER),
	LOCATION(ProcessorType.LOCATION),
	PLUGIN(ProcessorType.STRING),
	ITEM_NUMBER(ProcessorType.NUMBER),
	PAGE_NUMBER(ProcessorType.NUMBER),
	PAGE_TOTAL(ProcessorType.NUMBER),
	OWNER(ProcessorType.ENTITY),
	KILLER(ProcessorType.ENTITY),
	VIEWER(ProcessorType.ENTITY),
	TOOL(ProcessorType.ITEM_STACK),
	;

	private final ProcessorType processorType;

	// constructor
	Macro(ProcessorType processorType) {
		this.processorType = processorType;
	}

	@Override
	public ProcessorType getProcessorType() {
		return processorType;
	}

}
