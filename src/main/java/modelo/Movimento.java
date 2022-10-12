package modelo;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import util.Util;

@Entity
@Table(name = "movimento")

public class Movimento {
	private Long id;
	private double valor;
	private Calendar dataCriacao;
	private String tipo;

	// Um lance se refere a um único produto

	private Conta conta;

	// ********* Construtores *********

	public Movimento() {
	}

	public Movimento(double valor, Calendar dataCriacao, String tipo, Conta conta) {
		this.valor = valor;
		this.dataCriacao = dataCriacao;
		this.tipo = tipo;
		this.conta = conta;
	}

	// ********* Métodos do Tipo Get *********

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	@Column(nullable = false)
	public double getValor() {
		return valor;
	}

	@Transient
	public String getValorMasc() {
		return Util.doubleToStr(valor);
	}

	@Column(name = "DATA_CRIACAO")
	@Temporal(value = TemporalType.DATE)
	public Calendar getDataCriacao() {
		return dataCriacao;
	}

	public String getTipo() {
		return tipo;
	}

	@Transient
	public String getDataCriacaoMasc() {
		return Util.calendarToStr(dataCriacao);
	}

	// ********* Métodos do Tipo Set *********

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public void setDataCriacao(Calendar dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	// ********* Métodos para Associações *********

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTA_ID")
	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}
}