package com.advancejava.groupexercise.entity;

import com.advancejava.groupexercise.helper.RandomNumberGeneratorUtility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RegularAccount.class, name = "regular"),
        @JsonSubTypes.Type(value = CheckingAccount.class, name = "checking"),
        @JsonSubTypes.Type(value = InterestAccount.class, name = "interest")}
        )
public abstract class Account extends RandomNumberGeneratorUtility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonIgnore
    private String type;

    @NotNull
    private String name;

    @NotNull
    private String acctNumber;

    @NotNull
    private Double balance;

    @JsonIgnore
    @NotNull
    private Double minimumBalance;
    @JsonIgnore
    @NotNull
    private Double penalty;
    @JsonIgnore
    @NotNull
    private Double transactionCharge;
    @JsonIgnore
    @NotNull
    private Double interestCharge;

}
