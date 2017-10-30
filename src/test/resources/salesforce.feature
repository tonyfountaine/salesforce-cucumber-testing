Feature: log in to Salesforce

Scenario: As a user I would like to log in to Salesforce
    Given A username and password
    When Enter username and password
    Then  I should see the Home page
