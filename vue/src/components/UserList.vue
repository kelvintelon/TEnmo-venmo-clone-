<template>
  <div class="container">
    <h1 class="balance OutlineText">Your Balance: ${{ this.balance }}</h1>
    <table id="tblUsers">
      <thead>
        <tr>
          <th>Search Username</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>
            <input type="text" id="usernameFilter" v-model="filter.username" />
          </td>
        </tr>
        <tr v-for="user in filteredList" :key="user.userId">
          <td>{{ user.username }}</td>

          <td>
            <button v-on:click="makeForm(user.username, 2, user.userId, 2)">
              Send Money
            </button>
          </td>
          <td>
            <button>Request Money</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div>
      <form
        id="frmAddNewUser"
        v-show="showForm"
        v-on:submit.prevent="sendBucks()"
      >
        <div class="field">
          <label for="username">Username:</label>
          <input type="text" name="username" v-model="sendingTo" readonly />
        </div>
        <div class="field">
          <label for="amount">Amount:</label>
          <input type="number" name="amount" min="0" v-model="newTransfer.amount" />
        </div>
        <!-- <div class="field">
        <label for="username">Username:</label>
        <input type="text" name="username" v-model="newUser.username" />
      </div>
      <div class="field">
        <label for="emailAddress">Email Address:</label>
        <input type="text" name="emailAddress" v-model="newUser.emailAddress" />
      </div>-->
        <div class="twoButtons">
          <button type="submit" class="">Send Money</button>
          <button v-on:click="clearEverything()" class="">
            Cancel
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import accountService from "../services/AccountService";
import transferService from "../services/TransferService";
// import userService from "../services/AuthService";

export default {
  name: "user-list",
  props: {
    newList: Array,
  },
  data() {
    return {
      balance: 0,
      filter: {
        username: "",
        userId: "",
      },
      sendingTo: "",
      userLoggedIn: 0,
      newUsers: [],
      showForm: false,
      newTransfer: {
        transfer_type_id: 0,
        account_from: 0,
        account_to: 0,
        amount: 0,
        transfer_status_id: 0,
      },
    };
  },
  methods: {
    makeForm(username, number, userId) {
      this.showForm = !this.showForm;
      this.sendingTo = username;

      this.newTransfer.transfer_type_id = parseInt(number);
      this.newTransfer.transfer_status_id = parseInt(number);

      // finds ID of user logged in
      const UserString = localStorage.getItem("user");
      let firstIndex = UserString.indexOf("username");
      // let secondIndex = UserString.indexOf("password");
      this.userLoggedIn = parseInt(UserString.substring(6, firstIndex - 2));

      accountService.getAccountIdByUserId(userId).then((response) => {
        this.newTransfer.account_to = response.data;
        accountService
          .getAccountIdByUserId(this.userLoggedIn)
          .then((response2) => {
            this.newTransfer.account_from = response2.data;
          });
      });

      

      /* Tried to make this form a bit better */
      // if (this.showForm === false) {

      //   this.newTransfer.username= username
      // }

      // if (this.showForm == true) {
      //   if (this.newTransfer.username != username) {

      //   } else {
      //     this.showForm = false;
      //   }

      // }
    },
    sendBucks() {
    this.newTransfer.amount = parseInt(this.newTransfer.amount);

    // can't pass more than they have
    if (this.newTransfer.amount > this.balance) {
      alert("You cannot send more than what you have");
      this.newTransfer.amount = 0;
    } else {

    // send bucks aftewards
    transferService.sendBucks(this.newTransfer).then((response) => {
            if (response.status == 201) {
              alert("Your send bucks transaction was successful!")
              this.clearEverything();

              // update balance 
              accountService.getBalance().then((response) => {
      this.balance = response.data;
    });
            }
          })
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = 'Bad Request: Validation Errors';
            }
          });
    
    


    }

    
    },
    clearEverything() {
      this.showForm = false;
      this.newTransfer = {
        transfer_type_id: 0,
        account_from: 0,
        account_to: 0,
        amount: 0,
        transfer_status_id: 0,
      };
    },
    getNextUserId() {
      return this.nextUserId++;
    },
  },
  computed: {
    filteredList() {
      // let filteredUsers = this.newList;

      // if (this.filter.firstName != "") {
      //   filteredUsers = filteredUsers.filter((user) =>
      //     user.firstName
      //       .toLowerCase()
      //       .includes(this.filter.firstName.toLowerCase())
      //   );
      // }
      // if (this.filter.lastName != "") {
      //   filteredUsers = filteredUsers.filter((user) =>
      //     user.lastName
      //       .toLowerCase()
      //       .includes(this.filter.lastName.toLowerCase())
      //   );
      // }
      if (this.filter.username != "") {
        return this.$store.state.listOfUsers.filter((user) =>
          user.username
            .toLowerCase()
            .includes(this.filter.username.toLowerCase())
        );
      }
      // if (this.filter.emailAddress != "") {
      //   filteredUsers = filteredUsers.filter((user) =>
      //     user.emailAddress
      //       .toLowerCase()
      //       .includes(this.filter.emailAddress.toLowerCase())
      //   );
      // }
      // if (this.filter.status != "") {
      //   filteredUsers = filteredUsers.filter(
      //     (user) => user.status === this.filter.status
      //   );
      // }
      return this.$store.state.listOfUsers;
    },
  },
  mounted() {},
  created() {},
  beforeCreate() {
    accountService.getBalance().then((response) => {
      this.balance = response.data;
    });
  },
};
</script>

<style>
.twoButtons {
  margin-top: 15px;
}
.OutlineText {
  text-shadow:
    /* Outline 1 */ -1px -1px 0 #ffffff, 1px -1px 0 #ffffff,
    -1px 1px 0 #ffffff, 1px 1px 0 #ffffff, -2px 0 0 #ffffff, 2px 0 0 #ffffff,
    0 2px 0 #ffffff, 0 -2px 0 #ffffff, /* Outline 2 */ -2px -2px 0 #ffffff,
    2px -2px 0 #ffffff, -2px 2px 0 #ffffff, 2px 2px 0 #ffffff, -3px 0 0 #ffffff,
    3px 0 0 #ffffff, 0 3px 0 #ffffff, 0 -3px 0 #ffffff;
  /* Terminate with a semi-colon */
}

.balance {
  color: green;
  font-size: 50px;
}
table {
  margin-top: 20px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
    Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
  margin-bottom: 20px;
  background-color: white;
  border-radius: 20px;
}
form {
  background: white;
  padding: 12px;
  border-radius: 10px;
}
th {
  text-transform: uppercase;
}
td {
  padding: 15px;
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
