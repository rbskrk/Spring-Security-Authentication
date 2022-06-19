package com.mballem.curso.security.domain;

public enum PerfilTipo {
	
	USUARIO(1, "USUARIO");

	private long cod;
	private String desc;

	private PerfilTipo(long cod, String desc) {
		this.cod = cod;
		this.desc = desc;
	}

	public long getCod() {
		return cod;
	}

	public String getDesc() {
		return desc;
	}
}
