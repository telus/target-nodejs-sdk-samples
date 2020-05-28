# Target Local execution sample

## Overview

For this sample, we first created a simple AB activity for the `demo-marketing-offer1` mbox.  It has two experiences, each with JSON offer content.

### Experience A
```json
{
    "experience": "A",
    "asset": "demo-marketing-offer1-exp-A.png"
}
```
### Experience B

```json
{
    "experience": "B",
    "asset": "demo-marketing-offer1-exp-B.png"
}
```

As you can see, each experience has a different filename set in the `asset` property.

When run, the app server makes a getOffers call, requesting the `demo-marketing-offer1` mbox.  But the SDK has been configured to use local execution mode to determine the outcome of the call rather than send a request to the target delivery API.

When the page is loaded in a browser, an image is shown at the top of the page.  This image comes from one of the two experiences in the activity defined above.  The target response is also shown on the page.

## Running the sample
1. Install dependencies: `npm i`
2. Start: `npm start`
3. Point a browser to http://127.0.0.1:3000


## How it works

This sample utilizes local execution mode to determine target experiences.  By default, the SDK always makes a request to the target delivery API for each `getOffers` call.  But you can configure the SDK to use local execution mode instead.  This mode downloads target activity rules on initialization.   The rules are then used to determine which experiences to return when `getOffers` is called, rather than make a request to the delivery API each time.

There are four main properties to keep in mind when using local execution mode:

| Name                      | Description                                                                         |
|---------------------------|-------------------------------------------------------------------------------------|
| executionMode             | The execution mode the SDK will run in.  Can be `local`, `remote`, or `hybrid`. Defaults to `remote`      |
| artifactLocation          | This is a fully qualified url to the rules definition file that will be used to determine outcomes locally.  |
| artifactPayload           | A target decisioning JSON artifact. If specified, it is used instead of requesting one from a URL. |
| clientReadyCallback       | A callback function that will be invoked when the SDK is ready for getOffers method calls.  This is required for local execution mode.      |

NOTE: You must specify an `artifactLocation` or `artifactPayload` during the alpha release for local execution mode to work.

```js
const CONFIG = {
    executionMode: "local",
    artifactPayload: require("sampleRules"),
    clientReadyCallback: targetReady
};

const targetClient = TargetClient.create(CONFIG);

function targetReady() {
    // make getOffers requests
    // targetClient.getOffers({...})            
}
```

Once configured in this way, and after the clientReadyCallback has been invoked, an app can make standard SDK method calls as normal.


# Links:

Homework Docs: https://experienceleaguecommunities.adobe.com/t5/local-decisioning-node-js-sdk/ct-p/target-nodejs-sdk-alpha
Adobe Target activity: https://experience.adobe.com/#/@telus/target/activities/activitydetails/A-B/local-decisioning-testmay2820201310
