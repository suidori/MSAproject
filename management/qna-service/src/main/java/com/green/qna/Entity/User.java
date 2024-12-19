//package com.green.qna.Entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "user")
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long idx;
//
//    private String userid;
//
//    private String password;
//
//    private String name;
//
//    private String phoneNumber;
//
//    private String email;
//
//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//    private boolean accept;
//
//
////    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
////    private List<FreeBoard> list = new ArrayList<>(); //프리보드와 양방향 맵핑
//
//}
