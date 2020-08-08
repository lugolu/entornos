package ar.com.thinksoft.common;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public class Metodo {

	private boolean publico;
	private String name;
	private boolean ignoreMethod;
	private String remapName;
	private Parameter[] parameters;
	private String returnType;
	private Type genericReturnType;
	private String getter;
	private String setter;
	private Parameter[] setterParameters;
	private String listaExcel;
	private boolean heredado;
	private String remapSetter;

	public boolean isPublico() {
		return publico;
	}

	public void setPublico(boolean publico) {
		this.publico = publico;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIgnoreMethod() {
		return ignoreMethod;
	}

	public void setIgnoreMethod(boolean ignoreMethod) {
		this.ignoreMethod = ignoreMethod;
	}

	public String getRemapName() {
		return remapName;
	}

	public void setRemapName(String remapName) {
		this.remapName = remapName;
	}

	public Parameter[] getParameters() {
		return parameters;
	}

	public void setParameters(Parameter[] parameters) {
		this.parameters = parameters;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public Type getGenericReturnType() {
		return genericReturnType;
	}

	public void setGenericReturnType(Type genericReturnType) {
		this.genericReturnType = genericReturnType;
	}

	public String getGetter() {
		return getter;
	}

	public void setGetter(String getter) {
		this.getter = getter;
	}

	public String getSetter() {
		return setter;
	}

	public void setSetter(String setter) {
		this.setter = setter;
	}

	public Parameter[] getSetterParameters() {
		return setterParameters;
	}

	public void setSetterParameters(Parameter[] setterParameters) {
		this.setterParameters = setterParameters;
	}

	public String getListaExcel() {
		return listaExcel;
	}

	public void setListaExcel(String listaExcel) {
		this.listaExcel = listaExcel;
	}

	public boolean isHeredado() {
		return heredado;
	}

	public void setHeredado(boolean heredado) {
		this.heredado = heredado;
	}

	public String getRemapSetter() {
		return remapSetter;
	}

	public void setRemapSetter(String remapSetter) {
		this.remapSetter = remapSetter;
	}

}
