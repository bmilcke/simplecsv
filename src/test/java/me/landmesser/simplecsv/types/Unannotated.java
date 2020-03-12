package me.landmesser.simplecsv.types;

import java.time.LocalDate;

public class Unannotated {

  private String firstname;

  private String surname;

  private int age;

  private LocalDate dateOfBirth;

  private boolean member = false;

  private Boolean wantsEmail;

  private Integer memberSinceYear;

  private TestEnum testEnum;

  public Unannotated() {
  }

  // getter and setter

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public boolean isMember() {
    return member;
  }

  public void setMember(boolean member) {
    this.member = member;
  }

  public Boolean getWantsEmail() {
    return wantsEmail;
  }

  public void setWantsEmail(Boolean wantsEmail) {
    this.wantsEmail = wantsEmail;
  }

  public Integer getMemberSinceYear() {
    return memberSinceYear;
  }

  public void setMemberSinceYear(Integer memberSinceYear) {
    this.memberSinceYear = memberSinceYear;
  }

  public TestEnum getTestEnum() {
    return testEnum;
  }

  public void setTestEnum(TestEnum testEnum) {
    this.testEnum = testEnum;
  }
}
