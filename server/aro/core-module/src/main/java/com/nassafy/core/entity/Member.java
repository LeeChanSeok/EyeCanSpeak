package com.nassafy.core.entity;

import com.nassafy.core.DTO.ProviderType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
@DynamicUpdate
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NonNull
    private ProviderType providerType;

    @Column(updatable = false, unique = true, nullable = false)
    private String email;

    private String password;

    @Column(nullable = false)
    private String nickname;

    private Boolean alarm = true;

    private boolean auroraDisplay = true;

    private boolean cloudDisplay = true;

    private boolean auroraService = false;

    private boolean meteorService = false;

    private String refreshToken;
    private String fcmToken;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = {CascadeType.REMOVE})
    private List<Interest> interests = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.REMOVE})
    private List<Stamp> stamps = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private MeteorInterest meteorInterest;
    // 영속성 컨텍스트 비워주는 비지니스 메서드
    public void clearInterest(){
        this.interests = new ArrayList<>();
    }
    public void clearCountry() {
        this.meteorInterest = null;
    }

    public boolean getCloud(){ return this.cloudDisplay;}
    public boolean getAlarm(){
        return this.alarm;
    }

    public boolean getAuroraDisplay(){
        return this.auroraDisplay;
    }

    public void toggleAlarm(){
        this.alarm = !this.alarm;
    }

    public void toggleAuroraDisplay(){
        this.auroraDisplay = !this.auroraDisplay;
    }

    public void toggleCloudDisplay() {
        this.cloudDisplay = !this.cloudDisplay;}

    public boolean getAuroraService(){
        return this.auroraService;
    }

    public boolean getMeteorService(){
        return this.meteorService;
    }

    public void toggleAuroraService(){
        this.auroraService = !this.auroraService;
    }

    public void toggleMeteorService(){
        this.meteorService = !this.meteorService;
    }

    @Builder
    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
