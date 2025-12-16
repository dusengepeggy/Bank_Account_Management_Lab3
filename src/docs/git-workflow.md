git checkout -b ft-refactor
git add .
git commit -m "refactoring and changing the filing system"
git add .
git commit -m "Refactored Account and TransactionManager"
git push origin ft-refactor

git checkout  -b feature/exceptions
git add .
git commit -m "Added custom exceptions and enhanced error handling"
git push origin feature/exceptions

git checkout -b feature/testing
git add .
git commit -m "adding test and transaction between accounts"
git add .
git commit -m "testing logs"
git add .
git commit -m "fixing testing"
git add .
git commit -m "git workflow documentation"
git push origin feature/testing

git checkout main
git pull origin main 
git checkout -b feature/documentation
git cherry-pick 4e40730075f055d609c8532ef6a72d23115a0165
git add .
git commit -m "ft: git workflow documentation"
git pull origin main
git commit -m "fix: resolve conflict

