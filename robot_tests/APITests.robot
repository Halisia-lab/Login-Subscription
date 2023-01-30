*** Settings ***
Library           RequestsLibrary
Library           SeleniumLibrary
Library           JSONLibrary
Library           Collections

*** Variables ***
${URI}            http://localhost:8083

*** Test Cases ***
SAVE USER AND CONNECT
    SAVE USER    user3    password123
    CONNECT USER    user3    password123

GET USERS
    ${response}=    CONNECT USER    user3    password123
    Status Should Be    200    ${response}
    ${resultJson}=    GET Value From Json    ${response.json()}    accessToken
    ${token}=    Get From List    ${resultJson}    0
    GET USERS    ${token}

*** Keywords ***
CONNECT USER
    [Arguments]    ${username}    ${password}
    [Documentation]    This test case verifies that the response code of the POST Request should be 200,
    [Tags]    Regression
    Create Session    mysession    ${URI}    verify=true
    &{body}=    Create Dictionary    username=${username}    password=${password}
    &{header}=    Create Dictionary    Cache-Control=no-cache
    ${response}=    POST On Session    mysession    /login    data=${body}    headers=${header}
    Status Should Be    200    ${response}    #Check Status as 200
    [Return]    ${response}

SAVE USER
    [Arguments]    ${username}    ${password}
    [Documentation]    This test case verifies that the response code of the POST Request should be 201,
    [Tags]    Regression
    Create Session    mysession    ${URI}    verify=true
    &{form-data}=    Create Dictionary    firstName=prenom    lastName=nom    city=test    address=Address    email=email@email.com    phoneNumber=0123456789    username=${username}    postIndex=75000    password=${password}
    &{header}=    Create Dictionary    Cache-Control=no-cache    Content-Type=application/json;charset=UTF-8
    ${response}=    POST On Session    mysession    /user/save    json=${form-data}    headers=${header}
    Status Should Be    201    ${response}    #Check Status as 201

GET USERS
    [Arguments]    ${token}
    [Documentation]    This test case verifies that the response code of the GET Request should be 200,
    [Tags]    Regression
    Create Session    mysession    ${URI}    verify=true
    &{header}=    Create Dictionary    Cache-Control=no-cache    Authorization=Bearer ${token}    Content-Type=application/json
    ${response}=    Get On Session    mysession    /users    headers=${header}
    Status Should Be    200    ${response}    #Check Status as 200
    [Return]    ${response}
