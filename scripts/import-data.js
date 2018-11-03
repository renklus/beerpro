const data = require("./data.json");

const admin = require("firebase-admin");
const serviceAccount = require("./service-key.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://mge-android-testat.firebaseio.com"
});

data &&
  Object.keys(data).forEach(key => {
    const nestedContent = data[key];

    if (Array.isArray(nestedContent)) {
      console.log("Pushing new object");
      nestedContent.forEach(document => {
        admin
          .firestore()
          .collection(key)
          .add(document)
          .then(res => {
            console.log("Document successfully written!");
          })
          .catch(error => {
            console.error("Error writing document: ", error);
          });
      });
    } else if (typeof nestedContent === "object") {
      console.log("Setting new object");
      Object.keys(nestedContent).forEach(docTitle => {
        admin
          .firestore()
          .collection(key)
          .doc(docTitle)
          .set(nestedContent[docTitle])
          .then(res => {
            console.log("Document successfully written!");
          })
          .catch(error => {
            console.error("Error writing document: ", error);
          });
      });
    }
  });
