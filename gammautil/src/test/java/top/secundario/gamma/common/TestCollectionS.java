package top.secundario.gamma.common;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TestCollectionS {

    static class User {
        String name;
        String telephone;
        String gender;

        public User(String name, String telephone, String gender) {
            this.name = name;
            this.telephone = telephone;
            this.gender = gender;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", telephone='" + telephone + '\'' +
                    ", gender='" + gender + '\'' +
                    '}';
        }
    }

    @Test
    public void test_search() {
        List<User> userList = new ArrayList<>();

        User u1 = new User("Wang", "15712356047", "M");
        User u2 = new User("Li", "18940372153", "F");
        User u3 = new User("Zhao", "16930244578", "F");
        userList.add(u1);
        userList.add(u2);
        userList.add(u3);

        assertSame(CollectionS.search(userList, (_user) -> {return _user.telephone.endsWith("4578");}), u3);
        assertNull(CollectionS.search(userList, (_user) -> {return _user.name.equalsIgnoreCase("Liu");}));
    }
}
