<template>
  <div class="container">
    <table id="tblUsers">
      <thead>
        <tr>
          <th>Username</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>
            <input type="text" id="usernameFilter" v-model="filter.username" />
          </td>

          <td>&nbsp;</td>
        </tr>
        <tr
          v-for="user in filteredList"
          v-bind:key="user.id"
          v-bind:class="{ disabled: user.status === 'Disabled' }"
        >
          <td>{{ user.username }}</td>

          <td>
            <button>
              Send Money
            </button>
          </td>
          <td>
            <button>
              Request Money
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: "user-list",
  data() {
    return {
      filter: {
        firstName: "",
        lastName: "",
        username: "",
        emailAddress: "",
        status: "",
      },
      showForm: false,
      nextUserId: 7,
      newUser: {
        id: null,
        firstName: "",
        lastName: "",
        username: "",
        emailAddress: "",
        status: "Active",
      },
      selectedUserIDs: [],
      users: [
        {
          id: 1,
          firstName: "John",
          lastName: "Smith",
          username: "jsmith",
          emailAddress: "jsmith@gmail.com",
          status: "Active",
        },
        {
          id: 2,
          firstName: "Anna",
          lastName: "Bell",
          username: "abell",
          emailAddress: "abell@yahoo.com",
          status: "Active",
        },
        {
          id: 3,
          firstName: "George",
          lastName: "Best",
          username: "gbest",
          emailAddress: "gbest@gmail.com",
          status: "Disabled",
        },
        {
          id: 4,
          firstName: "Ben",
          lastName: "Carter",
          username: "bcarter",
          emailAddress: "bcarter@gmail.com",
          status: "Active",
        },
        {
          id: 5,
          firstName: "Katie",
          lastName: "Jackson",
          username: "kjackson",
          emailAddress: "kjackson@yahoo.com",
          status: "Active",
        },
        {
          id: 6,
          firstName: "Mark",
          lastName: "Smith",
          username: "msmith",
          emailAddress: "msmith@foo.com",
          status: "Disabled",
        },
      ],
    };
  },
  methods: {
    getNextUserId() {
      return this.nextUserId++;
    },
    saveUser() {
      this.newUser.id = this.getNextUserId();
      this.users.push(this.newUser);
      this.clearNewUser();
    },
    clearNewUser() {
      this.newUser = {
        id: null,
        firstName: "",
        lastName: "",
        username: "",
        emailAddress: "",
        status: "Active",
      };
    },
    deleteUser(id) {
      this.users = this.users.filter((user) => {
        return user.id !== id;
      });
    },
    flipStatus(id) {
      const index = this.findUserById(id);
      this.users[index].status =
        this.users[index].status === "Active" ? "Disabled" : "Active";
    },
    selectUser(event) {
      if (event.target.checked) {
        this.selectedUserIDs.push(parseInt(event.target.id));
      } else {
        this.selectedUserIDs = this.selectedUserIDs.filter((user) => {
          return user !== parseInt(event.target.id);
        });
      }
    },
    selectAll(event) {
      if (event.target.checked) {
        this.selectedUserIDs = []; //clear array of all selected users first
        this.users.forEach((user) => this.selectedUserIDs.push(user.id));
      } else {
        this.selectedUserIDs = [];
      }
    },
    enableSelectedUsers() {
      this.selectedUserIDs.forEach((id) => {
        this.users[this.findUserById(id)].status = "Active";
      });
      this.clearSelectedUsers();
    },
    disableSelectedUsers() {
      this.selectedUserIDs.forEach((id) => {
        this.users[this.findUserById(id)].status = "Disabled";
      });
      this.clearSelectedUsers();
    },
    deleteSelectedUsers() {
      this.selectedUserIDs.forEach((id) => {
        this.deleteUser(id);
      });
      this.clearSelectedUsers();
    },
    clearSelectedUsers() {
      this.selectedUserIDs = [];
    },
    findUserById(id) {
      return this.users.findIndex((user) => user.id == id);
    },
  },
  computed: {
    filteredList() {
      let filteredUsers = this.users;
      if (this.filter.firstName != "") {
        filteredUsers = filteredUsers.filter((user) =>
          user.firstName
            .toLowerCase()
            .includes(this.filter.firstName.toLowerCase())
        );
      }
      if (this.filter.lastName != "") {
        filteredUsers = filteredUsers.filter((user) =>
          user.lastName
            .toLowerCase()
            .includes(this.filter.lastName.toLowerCase())
        );
      }
      if (this.filter.username != "") {
        filteredUsers = filteredUsers.filter((user) =>
          user.username
            .toLowerCase()
            .includes(this.filter.username.toLowerCase())
        );
      }
      if (this.filter.emailAddress != "") {
        filteredUsers = filteredUsers.filter((user) =>
          user.emailAddress
            .toLowerCase()
            .includes(this.filter.emailAddress.toLowerCase())
        );
      }
      if (this.filter.status != "") {
        filteredUsers = filteredUsers.filter(
          (user) => user.status === this.filter.status
        );
      }
      return filteredUsers;
    },
    actionButtonDisabled() {
      return this.selectedUserIDs.length == 0;
    },
  },
};
</script>

<style scoped>
table {
  margin-top: 20px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
    Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
  margin-bottom: 20px;
}
th {
  text-transform: uppercase;
}
td {
  padding: 10px;
}
tr.disabled {
  color: red;
}
input,
select {
  font-size: 16px;
}

form {
  margin: 20px;
  width: 350px;
}
.field {
  padding: 10px 0px;
}
label {
  width: 140px;
  display: inline-block;
}
button {
  margin-right: 5px;
}
.all-actions {
  margin-bottom: 40px;
}
.btn.save {
  margin: 20px;
  float: right;
}
</style>
