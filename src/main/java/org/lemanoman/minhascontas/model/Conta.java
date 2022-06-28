package org.lemanoman.minhascontas.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CONTA")
public class Conta implements Serializable,DefaultModel {

   @Id
   @Column(name = "ID")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "VENCIMENTO")
   @Temporal(TemporalType.DATE)
   private Date vencimento;

   @Column(name = "DESCRICAO")
   private String descricao;

   @Column(name = "VALOR")
   private BigDecimal valor;

   @Column(name = "PAGO")
   private boolean pago;

   @Column(name = "USER_ID")
   private Long userId;

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Date getVencimento() {
      return vencimento;
   }

   public void setVencimento(Date vencimento) {
      this.vencimento = vencimento;
   }

   public String getDescricao() {
      return descricao;
   }

   public void setDescricao(String descricao) {
      this.descricao = descricao;
   }

   public BigDecimal getValor() {
      return valor;
   }

   public void setValor(BigDecimal valor) {
      this.valor = valor;
   }

   public boolean isPago() {
      return pago;
   }

   public void setPago(boolean pago) {
      this.pago = pago;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Conta conta = (Conta) o;

      return id != null ? id.equals(conta.id) : conta.id == null;
   }

   @Override
   public int hashCode() {
      return id != null ? id.hashCode() : 0;
   }
}
