package net.mononz.paragon.database;

import net.mononz.paragon.gson.GsAbilityType;
import net.mononz.paragon.gson.GsAffinity;
import net.mononz.paragon.gson.GsAttack;
import net.mononz.paragon.gson.GsHero;
import net.mononz.paragon.gson.GsHeroAbility;
import net.mononz.paragon.gson.GsHeroAffinity;
import net.mononz.paragon.gson.GsHeroTrait;
import net.mononz.paragon.gson.GsRole;
import net.mononz.paragon.gson.GsScaling;
import net.mononz.paragon.gson.GsType;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkInterface {

    @GET("v1/ability_type")
    Call<GsAbilityType[]> AbilityType();

    @GET("v1/affinity")
    Call<GsAffinity[]> Affinity();

    @GET("v1/attack")
    Call<GsAttack[]> Attack();

    @GET("v1/hero")
    Call<GsHero[]> Hero();

    @GET("v1/hero_ability")
    Call<GsHeroAbility[]> HeroAbility();

    @GET("v1/hero_affinity")
    Call<GsHeroAffinity[]> HeroAffinity();

    @GET("v1/hero_trait")
    Call<GsHeroTrait[]> HeroTrait();

    @GET("v1/role")
    Call<GsRole[]> Role();

    @GET("v1/scaling")
    Call<GsScaling[]> Scaling();

    @GET("v1/type")
    Call<GsType[]> Type();

}