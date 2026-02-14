USE threatintel;

INSERT INTO threats (title, description, severity)
VALUES
  ('Suspicious C2 Domain', 'Detected outbound traffic to known C2 domain. Investigate endpoints communicating with domain.', 'HIGH'),
  ('Credential Stuffing Activity', 'Multiple failed logins followed by successful access from new IP range.', 'MEDIUM'),
  ('Phishing Campaign', 'Employees received emails with malicious links masquerading as MFA prompts.', 'LOW');

