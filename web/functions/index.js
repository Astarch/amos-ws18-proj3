const cors = require('cors');
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

const collection = admin.firestore().collection('wordcountdata');


// Post request to add a trend to the firestore
// expected structure:
// {
//  query: "queryName",
//  method: "ALL",
//  timerange: "...",
//  data: {...}
//}
const addWordcountData = (request, response) => {
    console.log(request.body)
    const data = request.body
    if (!data || !data.query) {
        response.status(500).send(`Upload failed! No data`)
    } else {
        const query = data.query
        console.log("is " + query + " singleTerm: "+ isSingleTermQuery(query))
        let method = data.method ? data.method : "ANY"
        if(isSingleTermQuery(query)){
            method = "ANY"
        }
        const docName = data.query.toLowerCase() + "&method="+ method
        collection.doc(docName).set(Object.assign({}, data, {method}), {
                merge: true
            })
            .then(ref => response.status(200).send("Upload success"))
            .catch(error => response.status(500).send(`Upload failed ${error}`));
    }
}

const isSingleTermQuery = (query) => query.trim().indexOf(" ") < 0

// Get request to get trend data of one specific keyword (returns 404 if not in db)
// expected params: query and method
// answer: 
// {
//     ...    
// }
const getWordcountData = (request, response) => {
    console.log(request.query)
    const queryObj = request.query
    if(!queryObj || !queryObj.query || !queryObj.method){
        response.status(500).send(`Query or method missing!`)
    }else{
        const query = queryObj.query
        console.log("is " + query + " singleTerm: "+ isSingleTermQuery(query))
        let method = queryObj.method ? queryObj.method : "ANY"
        if(isSingleTermQuery(query)){
            method = "ANY"
        }
        const docName = query.toLowerCase() + "&method="+ method
        collection.doc(docName).get()
            .then(doc => {
                const data = doc.data()
                console.log(data)
                response.status(200).json(data.data)
            })
            .catch(error => response.status(404).send(`Could not get document with name ${docName}, error: ${error}`));
    }
}

// returns a list of all currently cached searchterms / keywords
// result:
// [ "term1", "term2", ...]
const getAllCachedTerms = (request, response) => {
    console.log(request.query)  
    collection.select('query', 'method').get()
        .then(snapshot => {
            const terms = []
            snapshot.forEach(doc =>terms.push(doc.data()))
            response.status(200).json(terms)
        })
        .catch(error => response.status(404).send(`Could not get terms, error: ${error}`));    
}
exports.addWordcountData = functions.https.onRequest((request, response) => {
    var corsFn = cors();
    corsFn(request, response, function () {
        addWordcountData(request, response);
    });
});

exports.getWordcountData = functions.https.onRequest((request, response) => {
    var corsFn = cors();
    corsFn(request, response, function () {
        getWordcountData(request, response);
    });
});

exports.getAllCachedTerms = functions.https.onRequest((request, response) => {
    var corsFn = cors();
    corsFn(request, response, function () {
        getAllCachedTerms(request, response);
    });
});
