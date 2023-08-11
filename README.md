# Task 1 "GIT & Build Tools"
  ## Gradle task
  1. Install Gradle
  2. Assemble custom jar `utils-1.3.5.jar`.
    It should be compatible with Java 8.
    The manifest file should contain the name and version of your jar.
    The jar should contain class `StringUtils` with method `boolean isPositiveNumber(String str)`.
    Use the `Apache Commons Lang 3.10` library to implement this method.
    Write one unit test for your `StringUtils.isPositiveNumber(String str)` using `JUnit 5.+`.
  3. Create a project `multi-project` with two subprojects `core` and `api`.
    The `core` subproject should contain class `Utils` with method `boolean isAllPositiveNumbers(String... str)`.
    Use `utils-1.3.5.jar` from the previous task to implement this method.
    The `api` subproject should contain the class `App` with the `main` method.
    Call `Utils.isAllPositiveNumbers("12", "79")` from the `main` method of `api` subproject.
  ## Git task
  1. The main purpose of this task was to understand what VCS(Git) is. 
  2. I have learnt how to:
      1. use basic git commands, such as `git commit`, `git push`, etc.
      2. make branches and manage them.
      3. use `rebase` and `cherry-pick`, etc.
