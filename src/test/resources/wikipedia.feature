Feature: search Wikipedia
 
  Scenario: direct search article
    Given Enter search term 'Cucumber'
    When Do search
    Then Single result is shown for 'Cucumber'