Feature: Salesforce Cases

Scenario: As a user I would like to create a new Case
    Given that I am logged in
     And I can see the HomePage
     And I click Cases tab
    When I click the new button
    When I enter the case info
    Then I should see the new Case 
