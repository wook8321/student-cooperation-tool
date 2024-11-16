package com.stool.studentcooperationtools;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProfileControllerTest {
    @DisplayName("real profile이 조회된다.")
    @Test
    void profile_return_real(){
        //given
        String expectedProfile = "real";
        MockEnvironment environment = new MockEnvironment();
        environment.addActiveProfile(expectedProfile);
        environment.addActiveProfile("oauth");
        environment.addActiveProfile("real-db");

        ProfileController controller = new ProfileController(environment);
        //when
        String profile = controller.profile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @DisplayName("real profile이 없으면 첫번째가 조회된다.")
    @Test
    void profile_return_first(){
        //given
        String expectedProfile = "oauth";
        MockEnvironment environment = new MockEnvironment();
        environment.addActiveProfile(expectedProfile);
        environment.addActiveProfile("real-db");
        ProfileController controller = new ProfileController(environment);

        //when
        String profile = controller.profile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @DisplayName("active profile이 없으면 default가 조회된다.")
    @Test
    void profile_return_default(){
        //given
        String expectedProfile = "default";
        MockEnvironment environment = new MockEnvironment();
        ProfileController controller = new ProfileController(environment);

        //when
        String profile = controller.profile();
        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }
}